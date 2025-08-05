package likelion13th.princess_edition.DTO.response;

import likelion13th.princess_edition.domain.Address;

// 사용자 주소 정보
public class AddressResponse {

    private String zipcode;
    private String address;
    private String addressDetail;

    public AddressResponse(String zipcode, String address, String addressDetail) {
        this.zipcode = zipcode;
        this.address = address;
        this.addressDetail = addressDetail;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    // Address 객체-> 응답
    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getZipcode(),
                address.getAddress(),
                address.getAddressDetail()
        );
    }
}