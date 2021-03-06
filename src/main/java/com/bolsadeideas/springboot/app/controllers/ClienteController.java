package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
public class ClienteController {

	@Autowired // Busca un beans con esa interfaz y lo inyecta
	private IClienteService clienteService;

	/**
	 * Listado de clientes
	 * 
	 * @param model
	 * @return List<Clientes>
	 */

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page,  Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 10);
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/", clientes);
		
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	/**
	 * Mostrar el formulario de registro
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/formulario")
	public String crear(Map<String, Object> model) {

		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de cliente");
		return "formulario";
	}

	/**
	 * Guardar un cliente
	 * 
	 * @param cliente
	 * @param result
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/formulario", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Listado de clientes");
			return "formulario";
		}

		clienteService.save(cliente);
		flash.addFlashAttribute("success", "Cliente creado con exito!");
		return "redirect:/";
	}



	@RequestMapping(value = "/formulario/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
		} else {
			return "redirect:listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "formulario";
	}

	/**
	 * Eliminar un registro
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			clienteService.delete(id);
		}
		flash.addFlashAttribute("danger", "Cliente eliminado con exito!");
		return "redirect:/";
	}

}
