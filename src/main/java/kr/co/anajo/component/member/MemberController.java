package kr.co.anajo.component.member;

import kr.co.anajo.context.annotation.Component;
import kr.co.anajo.context.annotation.DI;

@Component
public class MemberController {

	@DI
	private MemberService memberService;
}
