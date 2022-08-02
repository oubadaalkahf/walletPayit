package stg.payit.wallet.registration;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import lombok.AllArgsConstructor;
import stg.payit.wallet.appuser.AppUser;
import stg.payit.wallet.appuser.AppUserRepository;
import stg.payit.wallet.appuser.AppUserService;
import stg.payit.wallet.responseHandler.ResponseHandler;



@RestController
@CrossOrigin("*")
@RequestMapping(path = "registration")

@AllArgsConstructor
public class RegistrationController {

	private final RegistrationService registrationService;
	private final AppUserRepository appUserRepository;
	private final AppUserService appUserService;

	@PostMapping
	public ResponseEntity<Object> register(@RequestBody RegistrationRequest request) {
		if (appUserRepository.findByEmail(request.getEmail()).isPresent()) {
			return ResponseHandler.generateResponse("Email Already Exists", HttpStatus.OK, null);
		}
					
		return registrationService.register(request);
	}

	
	
	
	@PutMapping("changepassword")
	public ResponseEntity<Object>changePassword(@RequestBody RegistrationRequest registrationRequest) {
		Optional<AppUser> user = appUserRepository.findByEmail(registrationRequest.getEmail());
		if (user.isPresent()) {
			return appUserService.changePassword(user,registrationRequest);
		}
		else 		
		return ResponseHandler.generateResponseString("User not found", HttpStatus.OK);
	}

	@GetMapping(path = "confirm")
	public RedirectView confirm(@RequestParam("token") String token) {
		 registrationService.confirmToken(token);
		 RedirectView redirectView = new RedirectView();
			redirectView.setUrl("https://testingg.page.link/open");
			return redirectView;
	}
	


	@PostMapping(path = "fcm_token")
	public ResponseEntity<Object> fcm(
			@RequestParam("device_token") String fcmToken,
			@RequestParam("user_email") String email) {
		AppUser user = appUserRepository.findByEmail(email).get();
		return appUserService.addToken(fcmToken, user);

	}

	
	
	
	@PostMapping(path = "remove_fcm_token")
	public ResponseEntity<Object> remove_fcm(
				@RequestParam("user_email") String email) {
		AppUser user = appUserRepository.findByEmail(email).get();
		return appUserService.removeToken(user);

	}
	
	
	@GetMapping(path = "allUsers")
	public ResponseEntity<Object> allusers() {
		return appUserService.getUsers();
	}

	@GetMapping("user")
	public ResponseEntity<Object> loadUserByEmaill(@RequestParam("email") String email) {
		return appUserService.loadUserByemail(email);
	}
	@GetMapping("userByphone")
	public ResponseEntity<Object> loadUserByphone(@RequestParam("phone_number") String phone_number) {
		return appUserService.loadUserByPhoneNumber(phone_number);
	}
	@GetMapping("userByphonetransfer")
	public Optional<AppUser> loadUserByphonetransfer(@RequestParam("phone_number") String phone_number) {
		return appUserService.loadUserByPhoneNumbertransfer(phone_number);
	}
	
	@GetMapping("verifypn")
	public Boolean loadUserByphonee(@RequestParam("phone_number") String phone_number) {
		return appUserService.loadUserByPhoneNumberr(phone_number);
	}
	
	
	
	@GetMapping("verifycin")
	public Boolean existCin(@RequestParam("cin") String cin) {
		Optional<AppUser> user = appUserRepository.findByCin(cin);
		if (user.isPresent()) {
			return true;
		}
					
		return false;
	}
	
	@GetMapping("verifyEmail")
	public Boolean existEmail(@RequestParam("email") String email) {
		Optional<AppUser> user = appUserRepository.findByEmail(email);
		if (user.isPresent()) {
			return true;
		}
					
		return false;
	}
	
	
	@GetMapping("test")
	public String test() {
		return "working";
	}
	
	@PutMapping("retierSolde")
	public String retierSolde(@RequestParam("phone_number") String phone_number,@RequestParam("montant") double montant){
		return appUserService.retierSolde(phone_number,montant);
	}
	

}
