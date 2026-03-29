# Quickstart: Revisao de Javadoc UTF-8 do Backend

## 1. Confirmar o escopo

1. Trabalhe na branch `010-javadoc-utf8-review`.
2. Considere apenas classes de producao em `backend/src/main/java`.
3. Inclua classes geradas automaticamente que estejam materializadas nesse
   caminho.
4. Considere todos os pacotes sob `com.fidc.cdc.kogito` para `package-info.java`.
5. Trate UTF-8 como codificacao canonica para fontes, comentarios e HTML gerado.

## 2. Produzir o inventario inicial

1. Conte as classes Java em escopo:

   ```powershell
   (Get-ChildItem backend/src/main/java/com/fidc/cdc/kogito -Recurse -Filter *.java | Where-Object { $_.Name -ne 'package-info.java' } | Measure-Object).Count
   ```

2. Conte os contratos de pacote exigidos:

   ```powershell
   (Get-ChildItem backend/src/main/java/com/fidc/cdc/kogito -Directory -Recurse | Measure-Object).Count + 1
   ```

3. Use `specs/010-javadoc-utf8-review/research.md` como evidencia canonica da
   contagem final e do resultado do gate.

## 3. Remediar a documentacao

1. Adicione Javadoc de classe como contrato de API.
2. Crie ou revise `package-info.java` para cada pacote em escopo.
3. Garanta que os arquivos Java remediados sejam salvos em UTF-8.
4. Evite documentar internals acidentais; descreva apenas comportamento
   observavel e restricoes sustentadas pelo codigo.
5. Quando houver tags, mantenha a ordem e a coerencia com a assinatura.

## 4. Gerar e validar

1. Entre no modulo `backend`.
2. Execute o gate oficial:

   ```powershell
   mvn -DskipTests javadoc:javadoc
   ```

3. Se o Maven indicar que o artefato ja esta atualizado e voce precisar forcar
   uma geracao nova para evidencia, execute:

   ```powershell
   mvn -DskipTests clean javadoc:javadoc
   ```

4. Confirme `BUILD SUCCESS`.
5. Revise o HTML gerado em:

   ```text
   backend/target/site/apidocs/index.html
   ```

6. Confirme visualmente que a acentuacao e os caracteres especiais permanecem
   legiveis no HTML.

## 5. Encerrar a verificacao

1. Confirme que `115` classes em escopo aparecem no inventario final.
2. Confirme que `34` pacotes em escopo possuem `package-info.java`.
3. Confirme que o resultado final da validacao e `pass`.
4. Confirme que a evidência final registra a preservacao de UTF-8.
5. Registre a evidencia final em
   `specs/010-javadoc-utf8-review/research.md`.
6. Na evidencia final desta implementacao, o HTML gerado em
   `2026-03-29 14:04:17 -03:00` ficou em
   `backend/target/site/apidocs/index.html`.
