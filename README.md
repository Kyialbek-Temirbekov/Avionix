Here's the README.md file for your project:

# Online Flight Reservation Platform

## Description
An online flight reservation platform developed using a microservices architecture to ensure scalability and modularity. The platform provides flight search with advanced filtering options, secure online payments via Stripe integration, and automated ticket delivery via email including QR codes for airport check-in validation.

Key Features:
- Microservices architecture for scalability
- Flight search with advanced filtering
- Secure payment processing with Stripe
- Automated ticket delivery via email with QR codes
- Role-based access control
- OAuth2 authentication

## Technologies Used
- **Backend**: Java, Spring Boot, Spring Cloud
- **Security**: OAuth2, JWT
- **Database**: H2 (for development)
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Messaging**: RabbitMQ
- **Payment Processing**: Stripe API
- **QR Code Generation**: ZXing library
- **PDF Generation**: iText PDF
- **Email Service**: Spring Mail
- **Testing**: JUnit, Mockito

## Microservices
The system consists of several microservices:
1. **Client Service**: Handles user accounts, authentication, and profile management
2. **Flight Service**: Manages flight data, search, and booking
3. **Discovery Service**: Provides content like FAQs and terms of service
4. **Message Service**: Handles email notifications and ticket delivery
5. **Config Server**: Centralized configuration management
6. **Eureka Server**: Service discovery
7. **Gateway Server**: API gateway for routing requests

## Usage
The platform can be used to:
1. Search for flights with various filters
2. Book and pay for tickets
3. Receive e-tickets via email
4. Manage user accounts and profiles
5. View flight details and special offers

Each microservice can be built and run independently, with service discovery handled by Eureka. The API gateway provides a single entry point for all client requests.

## Getting Started
To run the project:
1. Start the Config Server and Eureka Server first
2. Then start other microservices in any order
3. The Gateway Server should be the entry point for all API requests

Detailed setup instructions for each service can be found in their respective directories.
