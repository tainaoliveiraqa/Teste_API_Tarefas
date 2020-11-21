import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class TesteApiContatos {
    //URIs
    String endpointContatos= "https://api-de-tarefas.herokuapp.com/contacts";
    String contatoID;

    //Dados do contato para cadastro
    String contatoParaCadastrar ="{\n" +
            "  \"name\": \"Taina\",\n" +
            "  \"last_name\": \"teste\",\n" +
            "  \"email\": \"testeqa@gmail.com\",\n" +
            "  \"age\": \"33\",\n" +
            "  \"phone\": \"01299990000\",\n" +
            "  \"address\": \"Rua teste\",\n" +
            "  \"state\": \"SP\",\n" +
            "  \"city\": \"SJC\"\n" +
            "}";

    @Test
    @DisplayName("Quando cadastrar um contato. Então na resposta deve retornar as informações do contato cadastrado.")
    public void cadastrarContato(){

        //POST Cadastrar um contato
        Response response =
        given()
                .contentType(ContentType.JSON)
                .accept("application/vnd.tasksmanager.v2")
                .body(contatoParaCadastrar)
        .when()
                 .post(endpointContatos);
        response.then()
                .statusCode(201)
                .log().all()
                .body("data.type", equalTo("contacts"))
                .body("data.attributes.name", equalTo("Taina"))
                .body("data.attributes.last-name", equalTo("teste"))
                .body("data.attributes.email", equalTo("testeqa@gmail.com"))
                .body("data.attributes.age", equalTo(33))
                .body("data.attributes.phone", equalTo("01299990000"))
                .body("data.attributes.address", equalTo("Rua teste"))
                .body("data.attributes.state", equalTo("SP"))
                .body("data.attributes.city", equalTo("SJC"));

        contatoID = response.jsonPath().getString("data.id");
        System.out.print("ID RETORNADO = " + contatoID);

        //Apagar contato recém criado
        excluirContatoCriado();

    }

    @Test
    @DisplayName("Quando consultar a lista de contatos. Então na resposta deve retornar os dados do contato cadastrado")
    public void consultarContatoPorID() {
        //Cadastrar Contato
        Response response =
        given()
                .contentType(ContentType.JSON)
                .body(contatoParaCadastrar)
        .when()
                .post(endpointContatos);
        response.then();
                contatoID = response.jsonPath().getString("data.id");
                System.out.print("ID RETORNADO = " + contatoID);
        //GET contato por ID
        when()
                .get(endpointContatos + "/"+ contatoID)
        .then()
                .statusCode(200)
                .log().all()
                .body("data.id",equalTo(contatoID))
                .body("data.type", equalTo("contacts"))
                .body("data.attributes.name", equalTo("Taina"))
                .body("data.attributes.last-name", equalTo("teste"))
                .body("data.attributes.email", equalTo("testeqa@gmail.com"))
                .body("data.attributes.age", equalTo(33))
                .body("data.attributes.phone", equalTo("01299990000"))
                .body("data.attributes.address", equalTo("Rua teste"))
                .body("data.attributes.state", equalTo("SP"))
                .body("data.attributes.city", equalTo("SJC"));

        //Apagar contato recém criado
        excluirContatoCriado();
    }

    //TODO PUT - Atualizar dados.
    @Test
    @DisplayName("Quando eu atualizar um contato. Então na resposta deve retornar os dados do contato atualizado.")
    public void atualizarContato(){
        String contatoParaAtualizar = " {\n" +
                "  \"name\": \"Taina\",\n" +
                "  \"last_name\": \"QA\",\n" +
                "  \"email\": \"testeqataina@gmail.com\",\n" +
                "  \"age\": \"33\",\n" +
                "  \"phone\": \"01299995555\",\n" +
                "  \"address\": \"Rua TesteQA\",\n" +
                "  \"state\": \"São Paulo\",\n" +
                "  \"city\": \"SJCampos\"\n" +
                "}";

        //Cadastrar Contato
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(contatoParaCadastrar)
                .when()
                        .post(endpointContatos);
        response.then();
                  contatoID = response.jsonPath().getString("data.id");
                  System.out.print("ID RETORNADO = " + contatoID);

        //atualizar dados
        given()
                .contentType(ContentType.JSON)
                .body(contatoParaAtualizar)
        .when()
                .put(endpointContatos + "/"+ contatoID)
                .then()
                .statusCode(200)
                .log().all()
                .body("data.id",equalTo(contatoID))
                .body("data.type", equalTo("contacts"))
                .body("data.attributes.name", equalTo("Taina"))
                .body("data.attributes.last-name", equalTo("QA"))
                .body("data.attributes.email", equalTo("testeqataina@gmail.com"))
                .body("data.attributes.age", equalTo(33))
                .body("data.attributes.phone", equalTo("01299995555"))
                .body("data.attributes.address", equalTo("Rua TesteQA"))
                .body("data.attributes.state", equalTo("São Paulo"))
                .body("data.attributes.city", equalTo("SJCampos"));


        //Apagar contato recém criado
        excluirContatoCriado();
    }

    @Test
    @DisplayName("Quando apagar um contato. Então seus dados devem ser removidos com sucesso.")
    public void excluirContatoporID(){
       //Cadastrar Contato
        Response response =
        given()
                .contentType(ContentType.JSON)
                .body(contatoParaCadastrar)
        .when()
                .post(endpointContatos);
        response.then();
        contatoID = response.jsonPath().getString("data.id");
        System.out.print("ID RETORNADO = " + contatoID);

        //excluir contato por id
        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(endpointContatos + "/"+ contatoID)
        .then()
                .statusCode(204);
                System.out.print("Contato Removido ID = " + contatoID);

         when()
            .get(endpointContatos+"/"+contatoID)
         .then()
            .statusCode(404)
            .body(equalTo("{\"status\":404,\"error\":\"Not Found\"}"));
       }

    //Método utilizado apenas para apagar dados de teste.
    public void excluirContatoCriado(){

        //excluir contato por id
        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(endpointContatos + "/"+ contatoID)
        .then()
                .statusCode(204);
        System.out.print("Contato Removido ID = " + contatoID);
    }

}