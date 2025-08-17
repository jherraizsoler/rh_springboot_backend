package gm.rh.controlador;

import gm.rh.excepcion.RecursoNoEncontradoException;
import gm.rh.modelo.Empleado;
import gm.rh.servicio.EmpleadoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
// Nombre de la app en la url
@RequestMapping("rh-app") // http://localhost:8080/rh-app
@CrossOrigin(value = "http://localhost:3000") // Puerto por default de Angular
public class EmpleadoControlador {
    private static final Logger logger = LoggerFactory.getLogger(EmpleadoControlador.class);

    @Autowired
    private EmpleadoServicio  empleadoServicio;

    @GetMapping("/empleados") //http://localhost:8080/rh-app/empleados
    public List<Empleado> obtenerEmpleados(){
       List<Empleado> empleados = this.empleadoServicio.listarEmpleados();
       //logger.info("Empleados obtenidos:");
       empleados.forEach(empleado -> logger.info(empleado.toString()));
       return empleados;
    }

    @PostMapping("/empleados") //http://localhost:8080/rh-app/Empleados
    public Empleado agregarEmpleado(@RequestBody Empleado empleado){
        //logger.info("Empleado a agregar: " + empleado);

        return this.empleadoServicio.guardarEmpleado(empleado);
    }

    @GetMapping("/empleados/{id}") //http://localhost:8080/rh-app/Empleados/id
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable int id
    ){
        Empleado empleado = this.empleadoServicio.buscarEmpleadoPorId(id);

        if(empleado == null){
            throw new RecursoNoEncontradoException("No se encontró el id: " + id);
        }

        return ResponseEntity.ok(empleado);
    }

    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(
            @PathVariable int id,
            @RequestBody Empleado empleadoRecibido
    ){
        Empleado empleado = this.empleadoServicio.buscarEmpleadoPorId(id);
        if(empleado == null){
            throw new RecursoNoEncontradoException("No se encontró el id: " + id);
        }
        empleado.setNombre(empleadoRecibido.getNombre());
        empleado.setDepartamento(empleadoRecibido.getDepartamento());
        empleado.setSueldo(empleadoRecibido.getSueldo());

        //Guardamos la información
        this.empleadoServicio.guardarEmpleado(empleado);

        return ResponseEntity.ok(empleado);

    }

    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Map<String,Boolean>> eliminarEmpleado(@PathVariable int id)
    {
        Empleado empleado = this.empleadoServicio.buscarEmpleadoPorId(id);
        if(empleado == null){
            throw new RecursoNoEncontradoException("No se encontró el id: " + id);
        }

        this.empleadoServicio.eliminarEmpleadoPorId(empleado.getIdEmpleado());
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado",Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }

}
