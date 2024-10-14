package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.exception.handler.UserErrorHandler;
import cherrysumer.cherrysumer.util.jwt.TokenProvider;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.web.dto.UserRequestDTO;
import cherrysumer.cherrysumer.web.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO.successLoginDTO userJoin(UserRequestDTO.userJoinRequestDTO request) throws ParseException {
        String password = hashPassword(request.getPassword());

        User user = new User();
        user.setLoginId(request.getLoginId());
        user.setName(request.getName());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPassword(password);
        user.setCategory(request.getCategory());
        user.setRegion(request.getRegion());
        user.setRegionCode(request.getRegionCode());
        user.setPoint(convertPoint(request.getLongitude(), request.getLatitude()));

        User newUser = userRepository.save(user);

        String token = tokenProvider.generateJwtToken(new UserRequestDTO.userInfoDTO(newUser.getId()));

        return new UserResponseDTO.successLoginDTO(token, newUser.getRegion(), newUser.getName());
    }

    @Override
    public Point convertPoint(String longitude, String latitude) throws ParseException {
        if(longitude == null || longitude.equals("") || latitude == null || latitude.equals(""))
            return null;

        Double lng = Double.parseDouble(longitude);
        Double lti = Double.parseDouble(latitude);

        String pointWKT = String.format("POINT(%s %s)", lng, lti);
        Point point = (Point) new WKTReader().read(pointWKT);

        return point;
    }

    @Override
    public void findUserId(String loginId) {
        if(userRepository.existsUserByLoginId(loginId))
            throw new UserErrorHandler(ErrorCode._LOGINID_CONFLICT);
    }

    @Override
    public void findNickname(String nickname) {
        if(userRepository.existsUserByNickname(nickname))
            throw new UserErrorHandler(ErrorCode._NICKNAME_CONFLICT);
    }

    @Override
    public UserResponseDTO.successLoginDTO userLogin(UserRequestDTO.userLoginRequestDTO request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new UserErrorHandler(ErrorCode._LOGIN_FAILURE));
        if(checkPassword(request.getPassword(), user)) {
            String token = tokenProvider.generateJwtToken(new UserRequestDTO.userInfoDTO(user.getId()));

            return new UserResponseDTO.successLoginDTO(token, user.getRegion(), user.getName());
        }
        throw new UserErrorHandler(ErrorCode._LOGIN_FAILURE);
    }

    // 아이디 찾기
    @Override
    public UserResponseDTO.userLoginIdDTO findLoginId(String email) {
        return findUserLoginId(email);
    }

    private UserResponseDTO.userLoginIdDTO findUserLoginId(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserErrorHandler(ErrorCode._MAIL_NOT_FOUND));

        return new UserResponseDTO.userLoginIdDTO(user.getLoginId());
    }

    // 사용자 인증
    @Override
    public void findUserPw(UserRequestDTO.findPwDTO request) {
        if(!findPw(request))
            throw new UserErrorHandler(ErrorCode._USER_NOT_FOUND);
    }

    private boolean findPw(UserRequestDTO.findPwDTO request) {
        User idUser = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new UserErrorHandler(ErrorCode._USER_NOT_FOUND));

        User emailUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserErrorHandler(ErrorCode._USER_NOT_FOUND));

        if(idUser.equals(emailUser)) {
            if(idUser.getName().equals(request.getName()) && emailUser.getName().equals(request.getName())) {
                return true;
            }
        }
        return false;
    }

    // 로그인 여부 확인, 토큰 만료 확인
    @Override
    public UserResponseDTO.successLoginDTO loginAuth() {
        User user = getLoggedInUser();
        return new UserResponseDTO.successLoginDTO(user.getRegion(), user.getName());
    }

    // 비밀번호 변경
    @Override
    public void changePw(UserRequestDTO.changePwDTO request) {
        changePasswd(request);
    }

    private void changePasswd(UserRequestDTO.changePwDTO request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new UserErrorHandler(ErrorCode._USER_NOT_FOUND));

        String pwd = hashPassword(request.getPassword());
        user.setPassword(pwd);
        userRepository.save(user);
    }

    /***
     * 비밀번호 암호화
     * @param pwd
     * @return
     */
    private String hashPassword(String pwd) {
        String password = passwordEncoder.encode(pwd);
        return password;
    }

    /***
     * 비밀번호 일치 여부 확인
     * @param pwd 사용자가 입력한 비밀번호
     * @param user user table에 저장된 암호화된 비밀번호
     * @return
     */
    private boolean checkPassword(String pwd, User user) {
        return passwordEncoder.matches(pwd, user.getPassword());
    }

    // 로그인 유저 가져오기
    @Override
    public User getLoggedInUser() {
        User user = getUser();
        return user;
    }

    // 토큰에서 유저 데이터 가져오기
    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UserErrorHandler(ErrorCode._USER_NOT_FOUND));
    }

}
