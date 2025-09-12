package api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import api.model.Medico;

import org.springframework.boot.CommandLineRunner;
import api.service.MedicoService;
import api.service.PacienteService;

import java.util.List;

@Configuration
public class SeedConfig {

    // Puedes desactivar el seeding con SEED_ENABLED=false en application.properties o variable de entorno
    @Value("${seed.enabled:true}")
    private boolean seedEnabled;

    @Bean
    CommandLineRunner seedInitialData(MedicoService medicos, PacienteService pacientes) {
        return args -> {
            if (!seedEnabled) return;

            // Solo sembrar si no hay datos aún
            if (medicos.listar().isEmpty()) {
                // Crear médicos iniciales
                Medico m1 = medicos.alta("Dr. Gregory House", "Diagnóstico");
                m1.setUsuario("house");
                m1.setPassword("house123");

                Medico m2 = medicos.alta("Dra. Meredith Grey", "Cirugía");
                m2.setUsuario("grey");
                m2.setPassword("grey123");

                Medico m3 = medicos.alta("Dr. Stephen Strange", "Neurocirugía");
                m3.setUsuario("strange");
                m3.setPassword("strange123");

                Medico m4 = medicos.alta("Dra. Lisa Cuddy", "Medicina Interna");
                m4.setUsuario("cuddy");
                m4.setPassword("cuddy123");

                Medico m5 = medicos.alta("Dr. John Watson", "Medicina Familiar");
                m5.setUsuario("watson");
                m5.setPassword("watson123");

                // Guardar lista con credenciales actualizadas
                List<Medico> lista = medicos.listar();
                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getId().equals(m1.getId())) lista.set(i, m1);
                    if (lista.get(i).getId().equals(m2.getId())) lista.set(i, m2);
                    if (lista.get(i).getId().equals(m3.getId())) lista.set(i, m3);
                    if (lista.get(i).getId().equals(m4.getId())) lista.set(i, m4);
                    if (lista.get(i).getId().equals(m5.getId())) lista.set(i, m5);
                }
                medicos.guardarTodos(lista);
            }

            if (pacientes.listar().isEmpty()) {
                pacientes.alta("Juan Pérez", "12345678A");
                pacientes.alta("María López", "98765432B");
                pacientes.alta("Carlos Sánchez", "11223344C");
                pacientes.alta("Lucía García", "55667788D");
                pacientes.alta("Pedro Ramírez", "44556677E");
                pacientes.alta("Ana Torres", "77889900F");
                pacientes.alta("Sofía Martín", "33445566G");
                pacientes.alta("Diego Navarro", "22113344H");
            }
        };
    }
}
