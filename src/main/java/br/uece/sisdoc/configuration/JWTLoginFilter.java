package br.uece.sisdoc.configuration;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.uece.sisdoc.model.Cargo;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.repository.UsuarioCargoRepository;
import br.uece.sisdoc.service.UsuarioService;

//filtra requisições de login
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private TokenAuthenticationService tokenAuthenticationService;
    
    UsuarioCargoRepository usuarioCargoRepository;
    
    @Autowired
    UsuarioService usuarioService;

    public JWTLoginFilter(String url, AuthenticationManager authenticationManager, UsuarioCargoRepository jpaRepository) {

         super(new AntPathRequestMatcher(url));

         setAuthenticationManager(authenticationManager);

         tokenAuthenticationService = new TokenAuthenticationService();
         
         this.usuarioCargoRepository = (UsuarioCargoRepository)jpaRepository;

    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
    	
    	httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
    	
    	if(httpServletRequest.getMethod().equals("OPTIONS")) {
    		
    		httpServletResponse.setHeader("Access-Control-Allow-Headers", "content-type");
    		httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
    		httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
    		httpServletResponse.setStatus(200);
    		return null;
    	}
    	
        AccountCredentials credentials = new ObjectMapper().readValue(httpServletRequest.getInputStream(), AccountCredentials.class);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        Authentication auth = getAuthenticationManager().authenticate(token);
        
        return auth;

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        String name = authentication.getName();

        tokenAuthenticationService.addAuthentication(response, name);
        
        
        //Formatando retorno da response
        JSONObject jsonResponse = new JSONObject();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        
        jsonResponse.put("JWT", response.getHeader("Authorization"));
        jsonResponse.put("usuario", getUsuarioJson(customUserPrincipal.getUsuario()));
        jsonResponse.put("permissoes", getPermissoesJson(customUserPrincipal.getUsuario()));
        
        response.getWriter().write(jsonResponse.toString());

    }
    
    private JSONObject getUsuarioJson(Usuario usuario) {
    	
    	JSONObject jsonUsuario = new JSONObject();
        jsonUsuario.put("email", usuario.getEmail());
        jsonUsuario.put("nome", usuario.getNome());
        jsonUsuario.put("tratamento", usuario.getTratamento());
        
        List<Cargo> cargos = usuarioCargoRepository.getUserCargos(usuario.getId());
        JSONArray jsonArrayCargos = new JSONArray();
        
        JSONObject cargoJson = null;
        JSONObject setorJson = null;
        for(Cargo cargo : cargos) {
        	cargoJson = new JSONObject();
        	setorJson = new JSONObject();
        	
        	cargoJson.put("cargoId", cargo.getId());
        	cargoJson.put("cargoNome", cargo.getNome());
        	
        	setorJson.put("setorId", cargo.getSetor().getId());
        	setorJson.put("setorNome", cargo.getSetor().getNome());
        	
        	cargoJson.put("setor", setorJson);
        	jsonArrayCargos.put(cargoJson);
        }
        
        jsonUsuario.put("cargos", jsonArrayCargos);
        
        jsonUsuario.put("id", usuario.getId());
        
        return jsonUsuario;
    	
    }
    
    private JSONArray getPermissoesJson(Usuario usuario) {
    	
    	JSONArray jsonArray = new JSONArray();
    	
    	JSONObject jsonPermissao = new JSONObject();
    	jsonPermissao.put("nome", "incluir anexo");
    	jsonPermissao.put("fullPath", "documento/incluir/anexo");
    	jsonPermissao.put("path", "incluir/anexo");
    	
    	jsonArray.put(jsonPermissao);
    	
    	return jsonArray;
    	
    }

}
