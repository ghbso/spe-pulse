# Sistema de Ponto Eletrônico

 ## Requisitos do sistema
1. Login do usuário 
 - O que deve fazer ? Realizar o login com seu usuário e senha, um token JWT deve ser entregue e todas as demais rotas devem ser autenticadas. 
 - Quais perfis de usuário tem permissão ? Todos

2. Cadastro de usuário
 - O que deve fazer ? Cadastrar um usuário informando: Nome,  Sobrenome, CPF, Email, Senha, Foto, Setor, Perfil
 - Quais perfis de usuário tem permissão ? ADMIN

3. Consulta de batidas de ponto na data informada
 - O que deve fazer ? Retornar os horários de registro de batida de ponto do dia da data informada.
 - Quais perfis de usuário tem permissão ? ADMIN ou USER (neste caso, pode consultar apenas seu ponto)

4. Consulta de batidas de ponto em um intervalo de tempo

> Registro normal de ponto: Jornada de 8h00 (oito horas): Entrada, ida e volta do almoço (intervalo) e saída do expediente. É obrigatório registrar o ponto 4 vezes por dia, com exceção do sábado.

> Registro de ponto no sábado: Registro correto do sábado, onde poderá haver duas marcações, e o que for registrado após o horário de saída (12:00), deverá ser considerado como hora extra. A quantidade máxima de horas extras pode dia é de 02 horas.

> Registro de apenas um ponto: Quando houver apenas 1 marcação de ponto, automaticamente o dia não deverá ser contabilizado, gerando assim débito para o colaborador naquele respectivo dia. 
 
> Registro de dois pontos: (dia de semana) No dia que houver apenas 02 registros do primeiro turno (entrada e ida para o intervalo), o segundo turno da jornada (retorno do intervalo e saída do expediente) será descontado em banco de horas e vice-versa, contabilizando assim somente 4 horas trabalhadas).

> Registro de três pontos: Quando houver 3 marcações o dia deverá ser computado como completo, com isso mesmo que o colaborador faça hora extra, essas horas não deverão ser computadas. 

> Apenas 4 pontos são computados, uma quinta batida é desconsiderada. 

 - O que deve fazer ? Retornar o total de horas trabalhadas, horas extras e horas não trabalhadas naquele intervalo para um dado usuário. 
 - Quais perfis de usuário tem permissão ? ADMIN ou USER (neste caso, pode consultar apenas seu ponto)

6. Consulta de batidas de ponto no mês atual
 - O que deve fazer ? Retornar o total de horas trabalhadas, horas extras e horas não trabalhadas no mês atual para um dado usuário. 
 - Quais perfis de usuário tem permissão ? ADMIN ou USER (neste caso, pode consultar apenas seu ponto)
 
7. Consulta de ocorrências da empresa
 > O registro de ponto incorreto acarreta numa **ocorrência**, pode ser
considerado ocorrência no sistema o registro de apenas *um ponto no dia*, porém essa regra pode ser alterada pelo admin para outra quantidade de pontos a escolha dele.
 - O que deve fazer ? Retornar a quantidade de ocorrências que cada um dos usuários cometeu nesse intervalo de tempo. 
 - Quais perfis de usuário tem permissão ? ADMIN 

8. Consulta de ocorrências de um usuário
 - O que deve fazer ? Retornar todas as ocorrências do usuário ligado ao CPF informado e os pontos batidos nos dias das ocorrências. 
 - Quais perfis de usuário tem permissão ? ADMIN ou USER (neste caso, pode consultar apenas seu ponto) 		  

9. Relatório de ocorrências consolidado por período
 - O que deve fazer ? Retornar, por dia da semana, os totais de ocorrências agrupados por setor e colaborador.
 - Quais perfis de usuário tem permissão ? MANAGER

 ## Tecnologias/Frameworks/Bibliotecas
Este projeto foi implementado em Java, utilizando o framework Spring Boot v2.4.2. Os testes de unidade foram realizados utilizando o JUnit 5 e as rotas foram documentadas com Swagger utilizando a Open API 3.
 
 ## Estruturação do projeto
O código fonte da aplicação encontra-se dentro da pasta **src/main**. 

O código fonte dos testes de unidade encontra-se dentro da pasta **src/test**. 

O schema da base de dados (PostgresSQL) encontra-se no arquivo **pulse-spe-schema.sql**, localizado na pasta **database**.

Os arquivos com *planejamento das atividades realizadas* e *overview da documentação gerada com swagger* estão localizados na pasta **documentos**.

 ## Testes de unidade dos requisitos do sistema

Os testes realizados neste projeto cobrem as camadas de controle, serviço e repositório da aplicação.  Abaixo são descritos quais testes unitários foram implementados:

 - [ ] Teste de nível de permissão para acesso à todas as rotas da aplicação.
       Estes testes estão descritos nas classes *PontoControllerTest* e
       *UsuarioControllerTest* localizadas dentro da pasta *pulse.spe.controller*.
       
 - [ ] Teste para a funcionalidade *Login do usuário*, descrito da classe
       *PontoControllerTest*.
       
 - [ ] Testes para as funcionalidades relacionadas às consultas de
       batida de ponto, descritos na classe *PontoServiceTest*,
       localizada dentro da pasta *pulse.spe.service*.
       
 - [ ] Testes para as funcionalidades *Consulta de ocorrências da
       empresa*, *Consulta de ocorrências de um usuário* e *Relatório de
       ocorrências consolidado por período*, descritos na classe
       *PontosRepoTest*, localizada dentro da pasta *pulse.spe.repository*.

