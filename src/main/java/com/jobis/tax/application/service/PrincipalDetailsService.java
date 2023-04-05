package com.jobis.tax.application.service;

import com.jobis.tax.application.security.dto.PrincipalDetails;
import com.jobis.tax.domain.user.entity.User;
import com.jobis.tax.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	private final UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.getByEmail(email);
		if(user == null) {
			return null;
		}else {
			return new PrincipalDetails(user);
		}
	}

}
