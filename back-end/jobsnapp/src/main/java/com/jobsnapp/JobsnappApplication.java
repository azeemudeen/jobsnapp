package com.jobsnapp;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jobsnapp.enumerations.RoleType;
import com.jobsnapp.model.Role;
import com.jobsnapp.model.User;
import com.jobsnapp.repositories.PictureRepository;
import com.jobsnapp.repositories.RoleRepository;
import com.jobsnapp.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class JobsnappApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobsnappApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(redirectConnector());
		return tomcat;
	}

	private Connector redirectConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("https");
		connector.setPort(8080);
		connector.setSecure(false);
		connector.setRedirectPort(8443);
		return connector;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/*").allowedOrigins("*");
			}
		};
	}

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository, PictureRepository pictureRepository, BCryptPasswordEncoder encoder) {
		return args -> {

			if(userRepository.findByRole(RoleType.ADMIN).size() == 0){
				Role admin_role = new Role(RoleType.ADMIN);
				roleRepository.save(admin_role);
				Role prof_role = new Role(RoleType.PROFESSIONAL);
				roleRepository.save(prof_role);

				User user = new User(
						"admin@mail.com",
						encoder.encode("012345"),
						"admin",
						"admin"
				);
				Set<Role> roles = new HashSet<Role>();
				roles.add(admin_role);
				roles.add(prof_role);
				user.setRoles(roles);
				userRepository.save(user);
			}
		};
	}

}
