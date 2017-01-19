package kr.co.anajo.component.auth;

import kr.co.anajo.context.annotation.Component;
import kr.co.anajo.context.annotation.RequestHandle;

@Component
public class LoginController {

	@RequestHandle(url = "/auth/login")
	public void loginPage() {
		System.out.println("*************************************");
	}
}
