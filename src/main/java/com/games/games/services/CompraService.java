package com.games.games.services;

import com.games.games.models.Compra;
import com.games.games.models.Juego;
import com.games.games.models.JuegosUsuario;
import com.games.games.models.Usuario;
import com.games.games.repositories.CompraRepository;
import com.games.games.repositories.JuegoRepository;
import com.games.games.repositories.JuegosUsuarioRepository;
import com.games.games.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;
    private final UsuarioRepository usuarioRepository;
    private final JuegoRepository juegoRepository;

    public Compra getCompraById(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));
    }

    public List<Compra> getAllCompras() {
        return compraRepository.findAll();
    }

    public List<Compra> getComprasByUsuario(Usuario usuario) {
        return compraRepository.findByUsuario(usuario);
    }

    public List<Compra> findByFechaCompraEntreFechas(Instant startDate, Instant endDate) {
        return compraRepository.findByFechaCompraBetween(startDate, endDate);
    }

    @Transactional
    public Compra hazCompra(Instant fechaCompra, Long juegoId, Long usuarioId) {
        // Validar que la operación sea positiva
        List<Juego> juegos = List.of(Juego.builder().id(1L).build());
        List<Usuario> usuarios = List.of(Usuario.builder().id(1L).build());
        if (juegos.size() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        if (usuarios.size() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        // Buscar el usuario por ID
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Buscar el juego por ID
        Juego juego = juegoRepository.findById(juegoId)
                .orElseThrow(() -> new IllegalArgumentException("Juego no encontrado"));

        // Crear la compra

        Compra compra = Compra.builder()
                .idCompra(1L)
                .fechaCompra(fechaCompra)
                .usuario(usuario)
                .juego(juego)
                .build();

        // Guardar la compra en base de datos
        Compra compraGuardada = compraRepository.save(compra); // Guarda y devuelve la compra guardada

        return compraGuardada; // Devuelve la compra realizada
    }

    public void cancelaCompra(Long purchaseId) {
        // Buscar la compra por ID
        Compra compra = compraRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada")); // Lanza una excepción si no existe

        // Obtener el juego asociado a la compra
        Juego juego = compra.getJuego(); // Obtiene el producto de la compra

        // Eliminar la compra
        compraRepository.delete(compra); // Elimina la compra
    }

    public List<Compra> findbyFechaCompraEntreFechas(Instant startDate, Instant endDate) {
        return compraRepository.findByFechaCompraBetween(startDate, endDate);
    }

}
