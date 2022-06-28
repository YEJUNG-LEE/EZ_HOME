package EZHome.service;

import EZHome.entity.Member;
import EZHome.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);

        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if (findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String Memb_email)
        throws UsernameNotFoundException {
            Member member = memberRepository.findByEmail(Memb_email) ;
            if(member == null){ // 회원이 존재하지 않는 경우
                throw new UsernameNotFoundException(member.getEmail()) ;
            }
            return User.builder()
                    .username(member.getEmail())
                    .password(member.getMemb_password())
                    .roles(member.getRole().toString())
                    .build();
    }
}