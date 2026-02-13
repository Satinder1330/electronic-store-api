package com.electronic.store;

import com.electronic.store.entities.Role;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.RoleRepository;
import com.electronic.store.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {
@Autowired
private RoleRepository roleRepository;
@Autowired
private UserRepository userRepository;
@Autowired
private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role role1 = roleRepository.findByName("ROLE_ADMIN").orElse(null);
		Role role2 = roleRepository.findByName("ROLE_USER").orElse(null);
		if(role1==null){
			role1 = new Role();
			role1.setRoleId(UUID.randomUUID().toString());
			role1.setName("ROLE_ADMIN");
			roleRepository.save(role1);
		}
		if(role2==null){
		role2 = new Role();
		role2.setRoleId(UUID.randomUUID().toString());
		role2.setName("ROLE_USER");
		roleRepository.save(role2);

		}

		//admin
		User admin = userRepository.findByEmail("admin123@gmail.com").orElse(null);
		if(admin==null){
			admin = new User();
			admin.setName("Admin");
			admin.setPassword(passwordEncoder.encode("admin"));
			admin.setGender("male");
			admin.setRoles(List.of(role1,role2));
			admin.setEmail("admin123@gmail.com");
			admin.setUserId(UUID.randomUUID().toString());
			userRepository.save(admin);
		}


	}
	@PostConstruct
	public void printEnv() {
		System.out.println("MYSQLHOST=" + System.getenv("MYSQLHOST"));
		System.out.println("MYSQLPORT=" + System.getenv("MYSQLPORT"));
		System.out.println("MYSQLDATABASE=" + System.getenv("MYSQLDATABASE"));
		System.out.println("MYSQLUSER=" + System.getenv("MYSQLUSER"));
		System.out.println("MYSQLPASSWORD=" + System.getenv("MYSQLPASSWORD"));
	}

}
