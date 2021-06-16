
# UserGen

Um aplicativo de geração de usuários 

---

Integrantes: Júlia Souza Braz e Renan Ribeiro Marcelino

O projeto usa a api [randomuser.me](https://randomuser.me) como o backend.
As funcionalidades providas são o aplicativo e o widget UserGen.

A geração de usuários é efetuada pelo uso da api URLConnection do java.

A ausência de uso de `AsyncTask`s no projeto 
é dada pela [deprecação da mesma](https://developer.android.com/reference/android/os/AsyncTask)

Artefatos utilizados incluem, mas não se limitam a:
- SQLite: Cache de usuários gerados
- Sensores: Sensor de proximidade é usado como método de navegação
- Fragments: Migração de telas de loading em exibição de usuário único
- Widget: Widget de geração de usuário
- Custom View: Botão customizado replicável
- REST: API randomuser.me
- GeoLocalização: Acesso às localizações dos usuários pelo google maps
- Android Jetpack: Uso de ViewModels e LiveData para comunicação entre fragments
- Junit 4: Uso de testes em modelos e classes de acesso
- Espresso: Uso de testes de interface gráfica automatizados
