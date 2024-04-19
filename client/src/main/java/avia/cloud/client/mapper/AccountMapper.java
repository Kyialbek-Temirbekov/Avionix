package avia.cloud.client.mapper;

import avia.cloud.client.dto.AccountDTO;
import avia.cloud.client.dto.CustomerDTO;
import avia.cloud.client.entity.Account;
import avia.cloud.client.entity.Customer;
import avia.cloud.client.util.ImageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface AccountMapper {
    @Mapping(source = "account", target = "account", qualifiedByName = "accountToAccountDTO")
    CustomerDTO customerToCustomerDTO(Customer customer);
    @Named("accountToAccountDTO")
    @Mapping(source = "image", target = "imageUrl", qualifiedByName = "encodeImage")
    AccountDTO accountToAccountDTO(Account account);
    @Named("encodeImage")
    default String encodeImage(byte[] imageBytes) {
        return ImageUtils.getBase64Image(imageBytes).substring(0,30);
    }
}
