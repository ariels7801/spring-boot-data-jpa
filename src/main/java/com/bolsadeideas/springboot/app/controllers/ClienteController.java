package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.Map;

@Controller
// Se guarda en sesion los datos  del objeto cliente
@SessionAttributes("cliente")
public class ClienteController {

    // Con esta anotación va a buscar un componente que implemente esta interfase (busca un bean) //
    @Autowired
    // Para definir una implementación concreta de agreaga el nombre que se le dio al respositorio //
    @Qualifier("clienteDaoJPA")
    private IClienteDao clienteDao;

    @RequestMapping(value = "listar", method = RequestMethod.GET)
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clienteDao.findAll());
        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {

        Cliente cliente = new Cliente();

        // En ves de enviarlo por addAttribute se usa put
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        clienteDao.save(cliente);
        // Borra de la session los datos del objeto cliente
        status.setComplete();
        return "redirect:listar";
    }

    @RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {

        Cliente cliente = null;

        if (id > 0) {
            cliente = clienteDao.findOne(id);
        } else {
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", "Editar Cliente");
        return "form";
    }


}
