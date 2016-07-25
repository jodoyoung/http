package kr.co.anajo.component.menu;

import kr.co.anajo.context.annotation.Component;
import kr.co.anajo.context.annotation.RequestHandle;

@Component
public class HomeController {

	@RequestHandle(url = "/home")
	public void home() {

	}
}