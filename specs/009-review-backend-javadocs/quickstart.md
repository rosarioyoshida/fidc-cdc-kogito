# Quickstart: Revisao de Javadoc do Backend

## 1. Confirmar o escopo

1. Trabalhe na branch `009-review-backend-javadocs`.
2. Considere apenas classes de producao em `backend/src/main/java`.
3. Inclua classes geradas automaticamente que estejam materializadas nesse
   caminho.
4. Considere todos os pacotes sob `com.fidc.cdc.kogito` para `package-info.java`.

## 2. Produzir o inventario inicial

1. Conte as classes Java em escopo:

   ```powershell
   (Get-ChildItem backend/src/main/java/com/fidc/cdc/kogito -Recurse -Filter *.java | Where-Object { $_.Name -ne 'package-info.java' } | Measure-Object).Count
   ```

2. Conte os contratos de pacote exigidos:

   ```powershell
   (Get-ChildItem backend/src/main/java/com/fidc/cdc/kogito -Directory -Recurse | Measure-Object).Count + 1
   ```

3. Use `specs/009-review-backend-javadocs/research.md` como evidencia canonica da
   contagem final e do resultado do gate.

## 3. Remediar a documentacao

1. Adicione Javadoc de classe como contrato de API.
2. Crie ou revise `package-info.java` para cada pacote em escopo.
3. Evite documentar internals acidentais; descreva apenas comportamento
   observavel e restricoes sustentadas pelo codigo.
4. Quando houver tags, mantenha a ordem e a coerencia com a assinatura.

## 4. Gerar e validar

1. Entre no modulo `backend`.
2. Execute o gate oficial:

   ```powershell
   mvn -DskipTests javadoc:javadoc
   ```

3. Confirme `BUILD SUCCESS`.
4. Revise o HTML gerado em:

   ```text
   backend/target/site/apidocs/index.html
   ```

## 5. Encerrar a verificacao

1. Confirme que `115` classes em escopo aparecem no inventario final.
2. Confirme que `34` pacotes em escopo possuem `package-info.java`.
3. Confirme que o resultado final da validacao e `pass`.
4. Registre a evidencia final em
   `specs/009-review-backend-javadocs/research.md`.
