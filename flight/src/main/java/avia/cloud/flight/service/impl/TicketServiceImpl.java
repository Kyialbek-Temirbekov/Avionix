package avia.cloud.flight.service.impl;

import avia.cloud.flight.dto.*;
import avia.cloud.flight.entity.Flight;
import avia.cloud.flight.entity.Segment;
import avia.cloud.flight.entity.Ticket;
import avia.cloud.flight.entity.enums.TicketStatus;
import avia.cloud.flight.exception.BadRequestException;
import avia.cloud.flight.exception.NotFoundException;
import avia.cloud.flight.repository.FlightRepository;
import avia.cloud.flight.repository.TicketRepository;
import avia.cloud.flight.service.IFlightService;
import avia.cloud.flight.service.ITicketService;
import avia.cloud.flight.service.client.UserFeignClient;
import avia.cloud.flight.util.ImageUtils;
import avia.cloud.flight.util.Messenger;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.html2pdf.HtmlConverter;
import com.stripe.exception.StripeException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService {
    private final UserFeignClient userFeignClient;
    private final IFlightService flightService;
    private final StripePaymentService paymentService;
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    private final Configuration freemarkerConfig;
    private final Messenger messenger;

    @Override
    public HashMap<String, Object> bookTicket(TicketBookRequest ticketBookRequest, String token) throws StripeException, IOException, TemplateException, WriterException {
        String status = paymentService.charge(ticketBookRequest.getChargeRequest());
        HashMap<String,Object> ticketDetails = createTicket(ticketBookRequest,token);
        sendTicketToEmail(ticketDetails);

        HashMap<String, Object> response = new HashMap<>();
        FlightDTO flight = flightService.convertToFlightDTO(((Ticket)ticketDetails.get("ticket")).getFlight(), "en");
        Ticket ticket = (Ticket) ticketDetails.get("ticket");
        response.put("paymentStatus", status);
        response.put("ticket", new TicketDTO(ticket.getId(), (CustomerDTO) ticketDetails.get("customer"), ticket.getSeat(), ticket.isCheckedBaggageIncluded(), ticket.getStatus(), flight));

        return response;
    }

    @Override
    public byte[] downloadTicket(String ticketId, String authToken) throws TemplateException, IOException, WriterException {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException("Ticket", "id", ticketId));
        CustomerDTO customer = userFeignClient.fetchAirline(ticket.getCustomerId(), authToken).getBody();
        HashMap<String,Object> ticketDetails = new HashMap<>();
        ticketDetails.put("ticket", ticket);
        ticketDetails.put("customer", customer);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(generateTemplate(ticketDetails),outputStream);
        return outputStream.toByteArray();
    }

    @Async
    protected void sendTicketToEmail(HashMap<String, Object> ticketDetails) throws TemplateException, IOException, WriterException {
        CustomerDTO customer = (CustomerDTO) ticketDetails.get("customer");
        String html = generateTemplate(ticketDetails);
        messenger.sendAttachmentMessage(new SimpleMailMessageDTO(
                customer.getAccount().getEmail(),
                "Avionix avia ticket",
                html
        ));
    }

    private String generateTemplate(HashMap<String, Object> ticketDetails) throws IOException, WriterException, TemplateException {
        Ticket savedTicket = (Ticket)ticketDetails.get("ticket");
        CustomerDTO customer = (CustomerDTO) ticketDetails.get("customer");
        Flight flight = savedTicket.getFlight();
        List<Segment> segments = flight.getDepartureSegment();
        LocalDateTime departureTime = segments.get(0).getTakeoffAt();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedDate = departureTime.format(dateFormatter);
        String formattedTime = departureTime.format(timeFormatter);

        String qr = generateQr(savedTicket.getId());
        // create template
        Map<String, Object> model = new HashMap<>();
        model.put("qr", qr);
        model.put("customer", customer.getFirstName() + " " + customer.getLastName());
        model.put("time", formattedTime);
        model.put("date", formattedDate);
        model.put("seat",savedTicket.getSeat());
        model.put("flight", flight.getNumber());
        model.put("gate", flight.getGate());
        model.put("destination", segments.get(segments.size() - 1).getArrivalIata());
        model.put("origin", segments.get(0).getTakeoffIata());

        Template template = freemarkerConfig.getTemplate("ticket.html");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
        return html;
    }

    private String generateQr(String id) throws WriterException, IOException {
        int size = 400;
        Writer writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(id, BarcodeFormat.QR_CODE, size, size);
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(new Color(240, 235, 228));
        graphics.fillRect(0, 0, size, size);
        graphics.setColor(new Color(14, 16, 54));
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (bitMatrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }
        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        ImageIO.write(image, "png", imageBytes);
        return ImageUtils.getBase64Image(imageBytes.toByteArray());
    }

    @Override
    public TicketDTO fetchTicket(String ticketId, String authToken, String lan) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException("Ticket", "id", ticketId));
        CustomerDTO customer = userFeignClient.fetchAirline(ticket.getCustomerId(), authToken).getBody();
        FlightDTO flight = flightService.convertToFlightDTO(ticket.getFlight(), lan);
        return new TicketDTO(ticket.getId(), customer, ticket.getSeat(), ticket.isCheckedBaggageIncluded(), ticket.getStatus(), flight);
    }

    @Override
    public void board(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException("Ticket", "id", ticketId));
        if(ticket.getStatus().equals(TicketStatus.RESERVED)) {
            ticket.setStatus(TicketStatus.BOARDED);
            ticketRepository.save(ticket);
        } else if (ticket.getStatus().equals(TicketStatus.BOARDED)) {
            throw new BadRequestException("Ticket is already boarded");
        } else {
            throw new BadRequestException("Only reserved tickets can be boarded. Ticket status is " + ticket.getStatus());
        }
    }

    @Override
    public List<TicketDTO> fetchCustomerTickets(String token, String lan) {
        String customerId = userFeignClient.findAccountId(token).getBody();
        List<Ticket> tickets = ticketRepository.findAllByCustomerId(customerId);
        return tickets.stream().map(ticket -> {
            FlightDTO flight = flightService.convertToFlightDTO(ticket.getFlight(), lan);
            return new TicketDTO(ticket.getId(), null, ticket.getSeat(), ticket.isCheckedBaggageIncluded(), ticket.getStatus(), flight);
        }).toList();
    }

    protected HashMap<String,Object> createTicket(TicketBookRequest ticketBookRequest, String token) {
        ResponseEntity<CustomerDTO> customerResponseEntity = userFeignClient.fetchCustomer(token);
        Ticket ticket = Ticket.builder()
                .flight(flightRepository.findById(ticketBookRequest.getFlightId()).orElseThrow( () ->
                        new NotFoundException("Flight", "id", ticketBookRequest.getFlightId())))
                .customerId(Objects.requireNonNull(customerResponseEntity.getBody()).getAccount().getId())
                .seat(ticketBookRequest.getSeat())
                .checkedBaggageIncluded(ticketBookRequest.isCheckedBaggageIncluded())
                .price(ticketBookRequest.getChargeRequest().getAmount())
                .status(TicketStatus.RESERVED)
                .build();
        Ticket savedTicket = ticketRepository.save(ticket);
        HashMap<String,Object> ticketDetails = new HashMap<>();
        ticketDetails.put("ticket", savedTicket);
        ticketDetails.put("customer", customerResponseEntity.getBody());
        return ticketDetails;
    }
}
