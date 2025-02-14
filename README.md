# OSGi

Изучаю спецификацию __OSGi__

## Модули

1. Модули `hello` и `hello-client` связаны с примерами из 1ой главы книги `OSGi in Action`.
2. Модули `non-module` и `is-module` связаны с примерами из 2ой главы книги `OSGI in Action`.

## Сборка проекта

~~~
mvn clean install
~~~

## Запуск Apache Felix

1. Скачать Apache Felix [Downloads](https://felix.apache.org/documentation/downloads.html).
   В примере использовалась версия 7.0.5.
2. Распаковать и перейти в папку `apache-felix`.
3. Запустить Apache Felix. Обрати внимание, запуск должен быть из директории `apache-felix`, а не `apache-felix/bin`.
   ~~~
   java -jar bin/felix.jar
   ~~~
4. Команды:
   ~~~
   felix:help          # Справка
   felix:lb            # Посмотреть список установленных bundle-ов
   felix:install <bundle-url>  # Установка bundle
   felix:start <bundle-id>     # Запуск bundle
   ~~~
5. Пример запуска `bundle` `hello-1.0.jar`.
   ~~~
   felix:install bundle/hello-1.0.jar    # Был выдан идентификатор 8
   felix:start 8
   ~~~

## Список литературы

1. Richard S. Hall, Karl Pauls, Stuart McCulloch, and David Savage. OSGi in Action. 2011
2. https://felix.apache.org/documentation/documentation.html
