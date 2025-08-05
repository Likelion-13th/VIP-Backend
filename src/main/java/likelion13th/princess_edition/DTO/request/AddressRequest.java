package likelion13th.princess_edition.DTO.request;

// 사용자 주소 정보 수정 시
public class AddressRequest {

    private String zipcode;       // 우편번호
    private String address;       // 기본 주소
    private String addressDetail; // 상세 주소

    public AddressRequest() {
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
}