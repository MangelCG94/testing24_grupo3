package com.games.games.services;

import com.games.games.models.Compra;
import com.games.games.models.JuegosUsuario;
import com.games.games.repositories.CompraRepository;
import com.games.games.repositories.JuegosUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;
    private final JuegosUsuarioRepository juegosUsuarioRepository;

    public Compra getCompraById(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));
    }

    public List<Compra> getAllCompras() {
        return compraRepository.findAll();
    }

    public List<Compra> getComprasByJuegosUsuario(JuegosUsuario juegosUsuario) {
        return compraRepository.findByJuegosUsuario(juegosUsuario);
    }

    public List<Compra> findByFechaCompraEntreFechas(Instant startDate, Instant endDate) {
        return compraRepository.findByFechaCompraEntre(startDate, endDate);
    }

    @Transactional
    public Compra hazCompra(Long fechaCompra, Long juegosUsuarioId) {
        // Validar que la operación sea positiva
        if (juegosUsuarioId <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        // Buscar el juego de usuario por ID
        JuegosUsuario juegosUsuario = juegosUsuarioRepository.findById(juegosUsuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Crear la compra

        Compra compra = Compra.builder()
                .idCompra(1L)
                .fechaCompra(fechaCompra)
                .juegosUsuario(juegosUsuario)
                .build();

        // Guardar la compra en base de datos
        Compra compraGuardada = compraRepository.save(compra); // Guarda y devuelve la compra guardada

        return compraGuardada; // Devuelve la compra realizada
    }

    public void cancelaCompra(Long purchaseId) {
        // Buscar la compra por ID
        Compra compra = compraRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada")); // Lanza una excepción si no existe

        // Obtener el juego de usuario asociado a la compra
        JuegosUsuario juegosUsuario = compra.getJuegosUsuario(); // Obtiene el producto de la compra

        // Eliminar la compra
        compraRepository.delete(compra); // Elimina la compra
    }

    public List<Compra> findbyFechaCompraEntreFechas(Instant startDate, Instant endDate) {
        return compraRepository.findByFechaCompraEntre(startDate, endDate);
    }
}
