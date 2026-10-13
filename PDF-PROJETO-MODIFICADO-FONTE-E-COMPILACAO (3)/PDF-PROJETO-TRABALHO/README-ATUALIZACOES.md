# PDF V12 — correções de perfil, câmera, mídia e notificações

## Alterações nesta revisão

- Mantém o título privado das notificações como `Nova notificação`, sem conteúdo de mensagem e sem ação para abrir a conversa ao tocar.
- Notificações de mensagens/mídias usam o canal de som padrão do Android (bip, sem vibração configurada pelo app); notificações de chamada usam canal silencioso.
- O `PdfMessagingService` recebe mensagens de dados do Firebase Cloud Messaging com a Activity fechada.
- O código de Cloud Functions envia avisos quando uma mensagem fica pronta e quando uma chamada recebe a oferta WebRTC.
- A câmera de vídeo solicita gravação em qualidade econômica, limite aproximado de 6,5 MB e duração de até 30 segundos, para tentar manter o arquivo abaixo do limite atual de 7 MB do transporte Firestore.
- O menu do clipe não tem mais a opção `Gravar vídeo`; a câmera ao lado do campo da mensagem continua abrindo `Tirar foto` e `Gravar vídeo`.
- As configurações incluem `Editar nome, número e foto`; ao mudar o número, o índice antigo é removido se pertencer ao usuário atual.
- Balão de áudio mais estreito, com controles compactos.

## Notificações push: etapa obrigatória no Firebase

O código Android sozinho não consegue enviar notificações push a outro celular. As funções em `functions/index.js` precisam estar implantadas no projeto Firebase `pdfapp-bb569`.

A implantação não é feita colando o README nem o código JavaScript nas regras do Firestore. Use o Firebase CLI num computador:

1. Instale o Node.js LTS e o Firebase CLI.
2. Abra um terminal na pasta `PDF-PROJETO`.
3. Execute `firebase login` e conclua o login Google.
4. Execute `firebase deploy --only functions`.
5. No Firebase Console, confirme que o Firestore, o Cloud Messaging e as permissões de notificação estão ativos. Cloud Functions pode exigir plano Blaze e faturamento habilitado.
6. Compile este projeto e instale o APK atualizado nos dois celulares.
7. Abra o PDF nos dois aparelhos, aceite a permissão de notificações no Android 13 ou superior e teste texto, mídia e chamadas com o app aberto, em segundo plano e com a tela bloqueada.

Se você só usa o site do Firebase e não tem acesso a um terminal, as funções ainda precisam ser implantadas por um computador ou por um fluxo de implantação compatível. Sem essa etapa, as notificações push não chegarão quando o aplicativo estiver fechado.

## Limites e testes

- O transporte atual usa blocos no Firestore e aceita até 7 MB por arquivo. A câmera recebe uma solicitação para limitar tamanho/qualidade, mas alguns aplicativos de câmera podem ignorar esses parâmetros. Se um vídeo continuar maior que 7 MB, ele será mantido localmente e o app avisará que precisa de um vídeo menor. Para suportar vídeos grandes de forma robusta, a evolução indicada é usar Firebase Storage com regras privadas e configurar o faturamento necessário.
- O Android pode atrasar notificações quando o usuário força a parada do aplicativo, nega permissões, restringe a bateria ou está sem conexão.
- O código foi ajustado estaticamente; não foi possível garantir compilação ou funcionamento sem executar o Gradle e testar em dois aparelhos reais. Compile no Android Studio antes de substituir a versão que funciona.


## Novas opções de Configurações (contagem e compartilhamento)

- A opção `Downloads` mostra uma contagem global de instalações registradas no primeiro início do app, usando o UID anônimo do Firebase. O app não grava nomes nem telefones para essa contagem. Reinstalações que gerem um novo UID podem contar novamente; portanto, a métrica representa instalações registradas, não cliques brutos no link de download.
- `Copiar link do PDF` copia a URL configurada no documento `app_config/download`, campo `url`. O botão só copia quando o campo contém uma URL HTTPS válida.
- Antes de testar, publique as novas `firestore.rules` e crie no Firestore o documento `app_config/download` com o campo `url` apontando para uma página/arquivo de download realmente publicado. O documento é somente leitura pelo aplicativo; configure-o pelo Firebase Console com uma conta administrativa.
- O APK ainda precisa ser compilado e testado. Não distribua o arquivo até validar a compilação, as regras e o link real.

## Ajuste de posição do botão de cópia

- O botão `Copiar link do PDF` agora aparece diretamente na tela principal, logo abaixo do cabeçalho, antes das abas `Conversas` e `Contatos`.
- O botão permanece visível sem precisar abrir Configurações. Ao tocar, copia a URL HTTPS configurada em `app_config/download` no Firestore.
- A opção `Downloads` permanece em Configurações.
- A compilação do APK não foi concluída neste ambiente porque o Gradle Wrapper precisa baixar o Gradle 9.4.1 e a conexão de rede está indisponível. Abra o projeto no Android Studio com acesso à internet e execute `assembleDebug` para gerar o APK de teste.
