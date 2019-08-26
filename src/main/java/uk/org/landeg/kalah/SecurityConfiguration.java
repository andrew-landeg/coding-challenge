//package uk.org.landeg.kalah;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//			.withUser("admin").password("password").roles("ADMIN")
//			.and()
//			.withUser("developer").password("password").roles("DEV");
//	}
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable()
//		.authorizeRequests()
//			.antMatchers("/").permitAll()
//			.and().authorizeRequests().antMatchers("/game/**").permitAll()
//			.and().authorizeRequests().antMatchers("/swagger-ui.html").hasAnyRole("ADMIN", "DEV")
//			.and()
//		.authorizeRequests()
//			.anyRequest().authenticated().and()
//		.formLogin()
//			.loginPage("/login")
//			.permitAll()
//		.and()
//			.logout();
////		.authorizeRequests().anyRequest().permitAll();
//	}
//}
