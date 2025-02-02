package com.skyler.catalogo.infra.user;

import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import com.skyler.catalogo.infra.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class UserController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final LojaRepository lojaRepository;
    @Value("${app.base-pass}")
    private String basePass;

    public UserController(UserRepository userRepository, JwtService jwtService, LojaRepository lojaRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.lojaRepository = lojaRepository;
    }

    @GetMapping("/private-route")
    public ResponseEntity privateRoute(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request){
        try{
            Optional<User> userOPT = this.userRepository.findByUsernameAndPassword(request.username(), request.password());
            if(userOPT.isEmpty()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            String jwt = jwtService.signToken(userOPT.get().getUsername());
            User user = userOPT.get();
            LoginResponse output =  new LoginResponse();
            output.setBeautyName(user.getBeautyName());
            output.setUsername(user.getUsername());
            output.setPassword(user.getPassword());
            output.setToken(jwt);
            output.setShouldChangeFirstPass(user.getPassword().equals(basePass));
            LoginResponse.Loja loja = null;
            if(user.getLoja()!=null){
                loja = new LoginResponse.Loja();
                loja.setNome(user.getLoja().getNome());
                loja.setSlug(user.getLoja().getSlug());
                loja.setSystemId(user.getLoja().getSystemId());
            }
            output.setLoja(loja);
            LoginResponse.Franquia franquia = new LoginResponse.Franquia();
            franquia.setNome(user.getFranquia().getNome());
            franquia.setSystemId(user.getFranquia().getSystemId());
            franquia.setIsMatriz(user.getFranquia().getIsMatriz());
            output.setFranquia(franquia);
            List<Loja> lojasFranquia = this.lojaRepository.findAllByFranquiaId(franquia.getSystemId());
            for(Loja lojaFranquia:lojasFranquia){
                loja = new LoginResponse.Loja();
                loja.setNome(lojaFranquia.getNome());
                loja.setSlug(lojaFranquia.getSlug());
                loja.setSystemId(lojaFranquia.getSystemId());
                output.addLojaFranquia(loja);
            }
            output.setRole(user.getRole());
            return ResponseEntity.ok().body(output);
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/login-by-loja")
    public ResponseEntity loginByLoja(@RequestBody LoginByLojaRequest request){
        try{
            Optional<User> userOPT = this.userRepository.findByLojaIdAndPassword(request.lojaSystemId(), request.password());
            if(userOPT.isEmpty()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            String jwt = jwtService.signToken(userOPT.get().getUsername());
            User user = userOPT.get();
            LoginResponse output =  new LoginResponse();
            output.setBeautyName(user.getBeautyName());
            output.setUsername(user.getUsername());
            output.setPassword(user.getPassword());
            output.setToken(jwt);
            output.setShouldChangeFirstPass(user.getPassword().equals(basePass));
            LoginResponse.Loja loja = null;
            if(user.getLoja()!=null){
                loja = new LoginResponse.Loja();
                loja.setNome(user.getLoja().getNome());
                loja.setSlug(user.getLoja().getSlug());
                loja.setSystemId(user.getLoja().getSystemId());
            }
            output.setLoja(loja);
            LoginResponse.Franquia franquia = new LoginResponse.Franquia();
            franquia.setNome(user.getFranquia().getNome());
            franquia.setSystemId(user.getFranquia().getSystemId());
            franquia.setIsMatriz(user.getFranquia().getIsMatriz());
            output.setFranquia(franquia);
            output.setRole(user.getRole());
            return ResponseEntity.ok().body(output);
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity changePass(HttpServletRequest request, String username , String newPass){
        try{
            if(newPass.equals(basePass)){
                return ResponseEntity.badRequest().body("A senha não pode ser a padrão");
            }
            User author = jwtService.getUserFromServelet(request);
            User user = userRepository.findByUsername(username).get();
            user.setPassword(newPass);
            userRepository.save(user);
            return ResponseEntity.status(200).build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
