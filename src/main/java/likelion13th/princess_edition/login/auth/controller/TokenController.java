package likelion13th.princess_edition.login.auth.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.princess_edition.global.api.ApiResponse;
import likelion13th.princess_edition.global.api.ErrorCode;
import likelion13th.princess_edition.global.api.SuccessCode;
import likelion13th.princess_edition.global.exception.GeneralException;
import likelion13th.princess_edition.login.auth.dto.JwtDto;
import likelion13th.princess_edition.login.dto.UserRequestDto;
import likelion13th.princess_edition.login.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name="토큰", description = "Access Token 및 Refresh Token 관련 API")
@RestController
@RequestMapping
@RequiredArgsConstructor

public class TokenController {

        private final UserService userService;
        @Operation(summary = "토큰 생성 (회원가입&로그인)", description = "provider_id 기반으로 토큰 반환")
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode="USER_200", description = "회원가입 & 로그인 성공")
        })
        @PostMapping
        public ApiResponse<JwtDto> generateToken(@RequestBody UserRequestDto.UserReqDto userReqDto) {
            try{
                String providerId = userReqDto.getProviderId();

                JwtDto jwt = userService.jwtMakeSave(providerId);

                return ApiResponse.onSuccess(SuccessCode.USER_LOGIN_SUCCESS, jwt);
            } catch (GeneralException e) {
                log.error("//회원가입, 로그인 중 에러 발견!{}", e.getReason().getMessage());
                throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }
