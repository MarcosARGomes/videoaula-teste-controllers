package br.com.algaworks.filmes.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import br.com.algaworks.filmes.model.Filme;
import br.com.algaworks.filmes.service.FilmeService;
import io.restassured.http.ContentType;

@WebMvcTest // notação para o teste ser rodado dentro de um contexto web.
public class FilmeControllerTest {

	@Autowired  // injetar a instancia do componente
	private FilmeController filmeController;
	
	@MockBean //dependencia do spring controler Mockada para criar uma imprementação falsa
	private FilmeService filmeService;
	
	@BeforeEach // colocar para ele rodar antes dos testes para controlar o contexto e não carregar todas as informações do projeto
	public void setup() {
		standaloneSetup(this.filmeController);
	}
	
	@Test//notação para ser reconhecido como um test
	public void deveRetornarSucesso_QuandoBuscarFilme() {

		//quando chamarmos filmeService ele ira retornar o thenReturn

		when(this.filmeService.obterFilme(1L))// chamada do metodo e parametros
			.thenReturn(new Filme(1L, "O Poderoso Chefão", "Sem descrição"));


		//quando aontecer alto com a
		given()
			.accept(ContentType.JSON)
		.when()
				// quando chegar uma requisição tipo get no endpoint /filmes, então o status code deve ser ok(200)
			.get("/filmes/{codigo}", 1L)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarNaoEncontrado_QuandoBuscarFilme() {
		
		when(this.filmeService.obterFilme(5L))
				.thenReturn(null);
		given()
			.accept(ContentType.JSON)
		.when()
			.get("/filmes/{ID}", 5L)
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarBadRequest_QuandoBuscarFilme() {
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get("/filmes/{codigo}", -1L)
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
		
		verify(this.filmeService, never()).obterFilme(-1L);
	}

	@Test
	public void deveRetornaralgo(){

	}
}
