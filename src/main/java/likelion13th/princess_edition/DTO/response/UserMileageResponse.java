package likelion13th.princess_edition.DTO.response;

// 마일리지 응답
public class UserMileageResponse {

    private int mileage;

    public UserMileageResponse(int mileage) {
        this.mileage = mileage;
    }

    public int getMileage() {
        return mileage;
    }
}
