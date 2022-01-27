/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	//1번 방법 , 필드에 주입하는거.
	//@Autowired
	// private OwnerRepository owners;

	//3번 방법 , 생성자에 붙여주는거. spring 에서 권장하는 방법. 생성자를 사용하는게 좋은 이유는 필수적으로 사용해야하는 레퍼런스 없이는 이 인스턴스를 만들지 못하도록 강제할 수 있음. 해당 클래스는 OwnerRepository 없이는 제대로 동작할 수 없음. 반드시 있어야 하는 객체인거지. 클래스 입장에서는.
	//그렇기 때문에 그걸 강제하기 위한 가장 좋은 수단인 생성자를 사용하는거야. 필드 인젝션이나 세터 인젝션은 이 인스턴스를 일단 만들 수는 있음. 이런 의존성 없이도. 그러한 단점이 있지만 그거 자체가 장점이 될 수도 있긴해. 순환 참조가 발생할 수 있음. 그니까 A는 B를, B는 A를. 이 경우에 둘 다 생성자 인젝션을 사용할 경우... 걍 못만들게돼... 이런 경우에 사용하는거야. 일단 인스턴스를 만들고 서로 주입하는거지.
	//하지만 가급적이면 서큘러 디펜던시가 발생하지 않도록 의존성을 조율하는게 좋아.
	private final OwnerRepository owners;

	//과제 petRepository 주입하기.
	private final PetRepository petRepository;

	private final ApplicationContext applicationContext;

	// 오너 컨트롤러 생성자에, 오너 리파지토리를 클리닉 서비스라는 이름으로 받아온 다음에 오너스라는 레퍼런스 변수에 설정을 해주고 있음.
	// 생성자를 통해 리파지토리를 받아오는거야. 그럼 Dependency 는 누가 넣어주냐? 그니까 오너 리파지토리는 누가 넣어주냐, 오너 컨트롤러 테스트
	// 파일로 가봐.
	public OwnerController(OwnerRepository clinicService, PetRepository petService, ApplicationContext applicationContext) {
		this.owners = clinicService;
		this.petRepository = petService;
		this.applicationContext = applicationContext;
	} // 다시 돌아와서, 오너 컨트롤러를 만들 때, 그 빈을 가져와서 주입을 해주는거야. 스프링의 IOC 컨테이너가 해주는거야. 스프링이 빈들의 의존성을
		// 관리하고 주입하는거지. 관리한다는게 필요한 의존성을 서로 주입해준다는거야. 특정한 생성자나 특정한 어노테이션을 보고.

	//2번 방법 , setter 에 붙여주는거.
//	@Autowired
//	public void setOwners(OwnerRepository owners) {
//		this.owners = owners;
//	}

	@GetMapping("/bean") // 요청을 핸들러로 매핑해주는 어노테이션
	@ResponseBody // return 값이 응답의 본문이 되도록 만들어주는 어노테이션
	public String bean() {
		return "Bean : " + applicationContext.getBean(OwnerRepository.class) + "\n"
			+ "Owners : " + this.owners;
		// 둘의 해시값은 같은 값이 나오는데, 이를 싱글톤 scope 의 객체라 한다. 객체 하나를 애플리케이션 전반에서 계속해서 재사용한다는 의미. 특히 멀티 스레드 상황에서 싱글톤 스코프를 구현하는거 자체는 번거롭고 조심스러운 일인데, IOC 컨테이너를 사용하면 손쉽게 소스코드에 추가적인 코드를 넣지 않아도 손쉽게 등록된 빈을 가져다 쓰면 된다.
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/owners/new")
	@LogExecutionTime //어노테이션을 달고 어노테이션 파일만 만든다고 해서 뭐가 되는건 아님. 그냥 주석에 불과함. 이 어노테이션을 읽어서 처리하는 무언가가 있어야 함. 그게 이제 만들 Aspect 임.
	public String initCreationForm(Map<String, Object> model) {
		Owner owner = new Owner();
		model.put("owner", owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.owners.save(owner);
			return "redirect:/owners/" + owner.getId();
		}
	}

	@GetMapping("/owners/find")
	@LogExecutionTime
	public String initFindForm(Map<String, Object> model) {
		model.put("owner", new Owner());
		return "owners/findOwners";
	}

	@GetMapping("/owners")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Owner owner, BindingResult result,
			Model model) {

		// allow parameterless GET request for /owners to return all records
		if (owner.getLastName() == null) { // 빈 값이면 모든 값이 검색되도록
			owner.setLastName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		String lastName = owner.getLastName();
		Page<Owner> ownersResults = findPaginatedForOwnersLastName(page, lastName);
		if (ownersResults.isEmpty()) {
			// no owners found
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		}
		else if (ownersResults.getTotalElements() == 1) {
			// 1 owner found
			owner = ownersResults.iterator().next();
			return "redirect:/owners/" + owner.getId();
		}
		else {
			// multiple owners found
			lastName = owner.getLastName();
			return addPaginationModel(page, model, lastName, ownersResults);
		}
	}

	private String addPaginationModel(int page, Model model, String lastName, Page<Owner> paginated) {
		model.addAttribute("listOwners", paginated);
		List<Owner> listOwners = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listOwners", listOwners);
		return "owners/ownersList";
	}

	private Page<Owner> findPaginatedForOwnersLastName(int page, String lastname) {

		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return owners.findByLastName(lastname, pageable);

	}

	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		Owner owner = this.owners.findById(ownerId);
		model.addAttribute(owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result,
			@PathVariable("ownerId") int ownerId) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		else {
			owner.setId(ownerId);
			this.owners.save(owner);
			return "redirect:/owners/{ownerId}";
		}
	}

	/**
	 * Custom handler for displaying an owner.
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
//	@GetMapping("/owners/{ownerId}")
//	@LogExecutionTime
//	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
//		ModelAndView mav = new ModelAndView("owners/ownerDetails");
//		Owner owner = this.owners.findById(ownerId);
//		mav.addObject(owner);
//		return mav;
//	}

}
