package likelion13th.princess_edition.service;

import likelion13th.princess_edition.DTO.request.AddressRequest;
import likelion13th.princess_edition.DTO.response.AddressResponse;
import likelion13th.princess_edition.domain.Address;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private Address address = new Address(); // 초기값 설정

    public AddressResponse getAddress() {
        return AddressResponse.from(address);
    }

    public void updateAddress(AddressRequest request) {
        address = new Address(
                request.getZipcode(),
                request.getAddress(),
                request.getAddressDetail()
        );
    }
}