package EZHome.config;

import EZHome.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //post 403에러 수정코딩

        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);;



        /* authorizeRequests : 시큐리티에서 request를 사용하고자 할때 사용하는 메소드입니다.
            permitAll() : 로그인 하지 않아도 접근 가능하게 할께요.
            anyRequest().authenticated() : 상기 위에서 열거한 내용 이외의 모든 항목든 인증을 요구합니다.
        * */
        http.authorizeRequests()

                .mvcMatchers("/", "/reEs/**", "/members/**", "/item/**", "/images/**", "/map/**", "/admin/**", "/service/**" , "/advertise/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("LREA")
                .anyRequest().authenticated();

        /* 인증 받지 못한 사용자가 접근 시도시 http 응답 코드 401을 보여 줍니다. */
        http.exceptionHandling().authenticationEntryPoint(new EZHome.config.CustomAuthenticationEntryPoint());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 다음 항목들은 인증 절차를 무시하도록 하겠씁니다.
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/resources/**", "/error", "/json/**", "/images/**") ;
    }

    @Autowired
    MemberService memberService ;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 인증 관리자(AuthenticationManager) 객체에 회원 정보를 읽어 오는 UserDetailService 객체를 지정해
        // 주어야 하는 데, memberService를 지정해줍니다.
        // 그리고, 암호화 객체도 지정해 주어야 하는데 passwordEncoder() 메소드를 호출하면 해결 됩니다.
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder()) ;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder() ;
    }
}
