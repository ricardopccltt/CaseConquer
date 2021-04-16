package com.caseconquer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.caseconquer.models.Register;
import com.caseconquer.repository.RegisterRepository;

@Controller
public class RegisterController {
	
	@Autowired
	private RegisterRepository rr;
	
	protected boolean _calcula_cpf(String cpf){
		int sum = 0;
		int count = 0;
		for(int i = 0; i < cpf.length(); i++)
		{
			if(Character.getNumericValue(cpf.charAt(i))>=0 && Character.getNumericValue(cpf.charAt(i)) <= 9)
			{
				count++;
				sum += Character.getNumericValue(cpf.charAt(i));
			}
		}
		
		String s = String.valueOf(sum);
		if(sum >= 10 && (s.charAt(0) == s.charAt(1) && count == 11))
		{
			return true;
		}
		return false;
	}
	
	@RequestMapping(value="/cadastrarAluno", method=RequestMethod.GET)
	public String form() {
		return "register/formRegister";
	}
	
	@RequestMapping(value={"/cadastrarAluno", "/{codigo}"}, method=RequestMethod.POST)
	public String form(Register register, RedirectAttributes attributes) {		
		if(register.getCodigo() == 0) {
			if(!_calcula_cpf(register.getCpf())) {
				//CPF valido
				attributes.addFlashAttribute("mensagem2", "CPF inválido.");
			} else if(rr.existsByEmail(register.getEmail())) {
				//Email existe
				attributes.addFlashAttribute("mensagem2", "Email já cadastrado.");
			} else if(rr.existsByCpf(register.getCpf())) {
				//CPF existe
				attributes.addFlashAttribute("mensagem2", "CPF já cadastrado.");
			} else {
				attributes.addFlashAttribute("mensagem", "Aluno cadastrado com sucesso!");
				rr.save(register);
			}
			return "redirect:/cadastrarAluno";
		}
		else {
			long code = register.getCodigo();
			String newEmail = register.getEmail();
			String oldEmail = rr.findByCodigo(code).getEmail();
			String newCpf = register.getCpf();
			String oldCpf = rr.findByCodigo(code).getCpf();
			if(!_calcula_cpf(register.getCpf())) {
				//CPF valido
				attributes.addFlashAttribute("mensagem2", "CPF inválido.");
			} 
			if(newEmail.equals(oldEmail) | newCpf.equals(oldCpf)) {
				register.setEmail("delete@gmail");
				register.setCpf("123456789");
				rr.save(register);
			}
			if(rr.existsByEmail(newEmail)) {
					//Email existe
					attributes.addFlashAttribute("mensagem2", "Email já cadastrado.");
					register.setCpf(oldCpf);
					register.setEmail(oldEmail);
					rr.save(register);
			}
			if(rr.existsByCpf(newCpf)) {
					//CPF existe
					attributes.addFlashAttribute("mensagem2", "CPF já cadastrado.");
					register.setCpf(oldCpf);
					register.setEmail(oldEmail);
					rr.save(register);
			}
			else {
				attributes.addFlashAttribute("mensagem", "Dados atualizados com sucesso!");
				register.setEmail(newEmail);
				register.setCpf(newCpf);
				rr.save(register);
			}
			return "redirect:/{codigo}";
		}
	}
	
	@RequestMapping("/cadastros")
	public ModelAndView studentsRegistered() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Register> register = rr.findAll();
		mv.addObject("registers", register);
		return mv;
	}
	
	@RequestMapping(value="/{codigo}", method=RequestMethod.GET)
	public ModelAndView detailsStudent(@PathVariable("codigo") long codigo) {
		Register register = rr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("register/detailsStudent");
		mv.addObject("register", register);
		return mv;
	}
		
	@RequestMapping("/delete")
	public String deleteStudent(long codigo) {
		Register register = rr.findByCodigo(codigo);
		rr.delete(register);
		return "redirect:/cadastros";
	}
}
