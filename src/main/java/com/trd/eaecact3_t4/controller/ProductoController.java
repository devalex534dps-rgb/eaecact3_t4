package com.trd.eaecact3_t4.controller;

import com.trd.eaecact3_t4.dto.ProductoDTO;
import com.trd.eaecact3_t4.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eaecact3")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // 1. LISTAR PRODUCTOS
    @GetMapping({ "", "/", "/productos" })
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "index";
    }

    // 2. MOSTRAR FORMULARIO DE CREACION
    @GetMapping({ "/nuevo", "/productos/nuevo" })
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("producto", new ProductoDTO());
        model.addAttribute("categorias", productoService.listarCategorias());
        return "form";
    }

    // 3. GUARDAR PRODUCTO
    @PostMapping({ "/guardar", "/productos/guardar" })
    public String guardar(@ModelAttribute("producto") ProductoDTO productoDTO) {
        productoService.guardar(productoDTO);
        return "redirect:/eaecact3";
    }

    // 4. MOSTRAR FORMULARIO DE EDICION
    @GetMapping({ "/editar/{id}", "/productos/editar/{id}" })
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        ProductoDTO producto = productoService.obtenerPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", productoService.listarCategorias());
        return "form";
    }

    // 5. ACTUALIZAR PRODUCTO
    @PostMapping({ "/actualizar/{id}", "/productos/actualizar/{id}" })
    public String actualizar(@PathVariable Integer id, @ModelAttribute("producto") ProductoDTO productoDTO) {
        productoDTO.setId(id);
        productoService.guardar(productoDTO);
        return "redirect:/eaecact3";
    }

    // 6. ELIMINAR PRODUCTO
    @GetMapping({ "/eliminar/{id}", "/productos/eliminar/{id}" })
    public String eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return "redirect:/eaecact3";
    }

    // 7. MOSTRAR DETALLE DE PRODUCTO
    @GetMapping({ "/detalle/{id}", "/productos/detalle/{id}" })
    public String mostrarDetalle(@PathVariable Integer id, Model model) {
        ProductoDTO producto = productoService.obtenerPorId(id);
        model.addAttribute("producto", producto);
        return "detalle";
    }
}
