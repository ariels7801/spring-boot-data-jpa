package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Controller
// Se guarda en sesion los datos  del objeto cliente
@SessionAttributes("cliente")
public class ClienteController {

    // Con esta anotación va a buscar un componente que implemente esta interfase (busca un bean) //
    @Autowired
    private IClienteService clienteService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    // :.+ es una expresion regular que evita que spring borre la extension del atributo
    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename){
        Path pathFoto = Paths.get("uploads").resolve(filename).toAbsolutePath();
        log.info("pathFoto: " + pathFoto);

        Resource recurso = null;
        try {
            recurso = new UrlResource(pathFoto.toUri());
            if (!recurso.exists() && !recurso.isReadable()){
                throw new RuntimeException("Error no se puede cargar la imagen " + pathFoto.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+recurso.getFilename() +"\"")
                .body(recurso);
    }

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash){
        Cliente cliente = clienteService.findOne(id);

        if (cliente == null){
            flash.addFlashAttribute("error", "El Id del cliente no existe!");
            return "redirect:/listar";
        }

        model.put("titulo", "Detalle cliente: " + cliente.getNombre());
        model.put("cliente", cliente);

        return "ver";
    }

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page,  Model model) {

        Pageable pageRequest = new PageRequest(page, 5);

        Page<Cliente> clientes = clienteService.findAll(pageRequest);

        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Cliente cliente = null;

        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", "El Id del cliente no existe!");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El Id del cliente no puede ser cero!");
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", "Editar Cliente");
        return "form";
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
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        if(!foto.isEmpty()){

            String uniqueFilenme = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
            Path rootPath = Paths.get("uploads").resolve(uniqueFilenme);

            Path rootAbsolutePath = rootPath.toAbsolutePath();
            log.info("rootPath: "+ rootPath);
            log.info("rootAbsolutePath: "+ rootAbsolutePath);


            try{

                Files.copy(foto.getInputStream(), rootAbsolutePath);

                flash.addFlashAttribute("info", "Ha subido correctamente " + uniqueFilenme);

                cliente.setFoto(uniqueFilenme);

            }catch(IOException e){
                e.printStackTrace();
            }
        }

        String mensajeFlash = (cliente.getId() != null)? "Cliente editado con exíto!" : "Cliente creado con exíto!";
        clienteService.save(cliente);
        // Borra de la session los datos del objeto cliente
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:listar";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash){
        if (id > 0) {
            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con exíto!");
        }
        return "redirect:/listar";
    }


}
