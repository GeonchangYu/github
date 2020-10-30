package bank.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import bank.model.Atm;
import bank.model.Common;
import bank.service.AtmService;
import bank.service.CommonService;

@Controller
public class AtmController {
	
	@Autowired
	private AtmService as;
	
	@Autowired
	private CommonService cs;
	
	@RequestMapping(value="accountChk")
	public String accountChk(String account_no, Model model, HttpSession session) {
		
		Common common = new Common();
		
		// 계좌번호 유효성 검사
		int result = as.accountChk(account_no);
		
		if(result > 0) {
			// 계좌번호로 회원번호, 계좌상태, 메시지 조회
			Atm atm = as.accountUserInfo(account_no);
			String account_state = atm.getAccount_state();
			
			if("0401".equals(account_state)) { // 계좌상태 : 정상
				session.setAttribute("user_no", atm.getUser_no());
				session.setAttribute("account_no", atm.getAccount_no());
				
				model.addAttribute("gbn", "main");
				
			} else { 
				common.setMsg_gbn("account");
				common.setMsg_code(atm.getAccount_state());
				
				// 구분, 코드로 메시지 조회
				String msg = cs.getMessage(common);
				model.addAttribute("msg" , msg);
				return "error";
			}

			return "atmMain";
		} else {
			common.setMsg_gbn("account");
			common.setMsg_code("0000");

			// 구분, 코드로 메시지 조회
			String msg = cs.getMessage(common);
			msg = msg.replace(".", ".<br>");
			System.out.println(msg);
			model.addAttribute("msg" , msg);
			return "error";
		}
		
	}
	
}
