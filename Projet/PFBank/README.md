# Akka Remote

Exemple de programme en Java pour créer et utiliser des Acteurs Akka répartis sur plusieurs JVM.

Pour compiler le code, utiliser Maven avec les commandes suivantes :

```
mvn clean compile install
```

Lancer d'abord la banque :

```
mvn -pl banque -Dexec.mainClass="com.perfect.bank"
```

Lancer ensuite des banquiers :

```
mvn -pl banker -Dexec.mainClass="com.perfect.bank"
```

Lancer enfin des clients :

```
mvn -pl client -Dexec.mainClass="com.perfect.bank"
```
