package bleuauction.bleuauction_be.server.common.jwt;


import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member loginMemebr =
                memberRepository
                        .findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("cannot found Email"));
        log.info("loadUserByUsername() - loginMemebr: >>>>>>>>>>>>>>>> {}", loginMemebr);

        APIUserDTO apiUserDTO =
                new APIUserDTO(
                        loginMemebr.getId(),
                        loginMemebr.getEmail(),
                        loginMemebr.getPassword(),
                        loginMemebr.getName(),
                        loginMemebr.getCategory());
        log.info("loadUserByUsername() - apiUserDTO: >>>>>>>>>>>>>>>> {}", apiUserDTO);

        return apiUserDTO;
    }
}
