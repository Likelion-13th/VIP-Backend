package likelion13th.princess_edition.Controller;

import likelion13th.princess_edition.DTO.response.AddressResponse;
import likelion13th.princess_edition.DTO.response.UserInfoResponse;
import likelion13th.princess_edition.DTO.response.UserMileageResponse;
import likelion13th.princess_edition.login.auth.jwt.CustomUserDetails;
import likelion13th.princess_edition.service.UserAddressService;
import likelion13th.princess_edition.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;
    private final UserAddressService userAddressService;

    // 프로필 정보 보기
    @GetMapping("/profile")
    public UserInfoResponse getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getUserInfo(userDetails.getUser());
    }

    // 마일리지 보기
    @GetMapping("/mileage")
    public UserMileageResponse getMileage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getUserMileage(userDetails.getUser());
    }

    // 주소 보기
    @GetMapping("/address")
    public AddressResponse getAddress(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userAddressService.getUserAddress(userDetails.getUser());
    }
}