package com.trd.eaecact3_t4.service;

import com.trd.eaecact3_t4.dto.ProductoDTO;
import com.trd.eaecact3_t4.entity.Categoria;
import com.trd.eaecact3_t4.entity.Producto;
import com.trd.eaecact3_t4.repository.CategoriaRepository;
import com.trd.eaecact3_t4.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // LISTAR PRODUCTOS
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // OBTENER PRODUCTO POR ID
    @Transactional(readOnly = true)
    public ProductoDTO obtenerPorId(Integer id) {
        return productoRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    // CREAR O ACTUALIZAR PRODUCTO
    @Transactional
    public ProductoDTO guardar(ProductoDTO dto) {
        Producto producto;
        if (dto.getId() != null) {
            producto = productoRepository.findById(dto.getId()).orElse(new Producto());
        } else {
            producto = new Producto();
        }

        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + dto.getCategoriaId()));
            producto.setCategoria(categoria);
        }

        Producto guardado = productoRepository.save(producto);
        return convertirADTO(guardado);
    }

    // ELIMINAR PRODUCTO
    @Transactional
    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }

    // LISTAR CATEGORIAS (para poblar selectores en vistas sin exponer el repositorio al controlador)
    @Transactional(readOnly = true)
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    // OBTENER CATEGORIA POR ID
    @Transactional(readOnly = true)
    public Categoria obtenerCategoriaPorId(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    // GUARDAR CATEGORIA
    @Transactional
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // ELIMINAR CATEGORIA
    @Transactional
    public void eliminarCategoria(Integer id) {
        categoriaRepository.deleteById(id);
    }

    // MAPEO ENTITY -> DTO
    private ProductoDTO convertirADTO(Producto producto) {
        Integer categoriaId = producto.getCategoria() != null ? producto.getCategoria().getId() : null;
        String categoriaNombre = producto.getCategoria() != null ? producto.getCategoria().getNombre() : null;
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                categoriaId,
                categoriaNombre
        );
    }
}

