package likelion13th.princess_edition.DTO.response;

// 사용자 정보 응답할 때
public class UserInfoResponse {

    private String nickname;
    private String zipcode;
    private String address;
    private String addressDetail;
    private int recentTotal;

    public UserInfoResponse(String nickname, String zipcode, String address,
                            String addressDetail, int recentTotal) {
        this.nickname = nickname;
        this.zipcode = zipcode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.recentTotal = recentTotal;
    }

    public String getNickname() {
        return nickname;
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

    public int getRecentTotal() {
        return recentTotal;
    }
}