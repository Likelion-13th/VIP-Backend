package likelion13th.princess_edition.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String addressDetail;

    // 기본 생성자 - 초기값 설정
    public Address() {
        this.zipcode = "10540";
        this.address = "경기도 고양시 덕양구 항공대학로 76";
        this.addressDetail = "한국항공대학교";
    }

    // 전체 필드를 받는 생성자
    public Address(String zipcode, String address, String addressDetail) {
        this.zipcode = zipcode;
        this.address = address;
        this.addressDetail = addressDetail;
    }

    // Getter 메서드들
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
