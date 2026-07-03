package org.example.controller;

import org.example.model.dto.LoginDTO;
import org.example.model.entity.Usuario;
import org.example.repository.UsuarioRepository;
import org.example.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Ruta pública liberada en tu SecurityConfig
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager; // El motor que valida las credenciales

    @Autowired
    private JwtProvider jwtProvider; // Nuestro fabricante de tokens

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO dto) { // <-- Cambiado de Map a LoginDTO

        Usuario usuario = usuarioRepository.findByUsernameAndPassword(dto.getUsername(),dto.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("Usuario y/o password incorrectos"));

        // Credencial valida, generar token
        String tokenGenerado = jwtProvider.generarToken(usuario.getUsername());

        // Mandamos el token en json
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("token", tokenGenerado);

        return ResponseEntity.ok(respuesta);
    }
}