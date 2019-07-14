# WordToVec

Distributed representations (DR) of words (i.e., word embeddings) are used to capture semantic and syntactic regularities of the language by analyzing distributions of word relations within the textual data. Modeling methods generating DRs rely on the assumption that 'words that occur in similar contexts tend to have similar meanings' (distributional hypothesis) which stems from the nature of language itself. Due to their unsupervised nature, these modeling methods do not require any human judgement input to train, which allows researchers to train very large datasets in relatively low costs.

SkipGram is a prediction based distributional semantic model (DSM) consisting of a shallow neural network architecture inspired from neural language modeling (LM) intuitions. It is commonly known for its open-source implementation library word2vec. SkipGram acts like a log-linear classifier maximizing the prediction of the surrounding words of a word within a context (center window). Probabilistic word and sentence prediction by local neighbors of a word has been successfully applied on LM tasks under Markov assumption. SkipGram leverages the same idea by considering the words within the window as positive and negative instances and learning weights (for k contexts) which maximizes word predictions. In the training process, each word vector starts as a random vector, and then iteratively shifts to the neighboring vector.

For Developers
============

## Requirements

* [Java Development Kit 8 or higher](#java), Open JDK or Oracle JDK
* [Maven](#maven)
* [Git](#git)

### Java 

To check if you have a compatible version of Java installed, use the following command:

    java -version
    
If you don't have a compatible version, you can download either [Oracle JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or [OpenJDK](https://openjdk.java.net/install/)    

### Maven
To check if you have Maven installed, use the following command:

    mvn --version
    
To install Maven, you can follow the instructions [here](https://maven.apache.org/install.html).      

### Git

Install the [latest version of Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

## Download Code

In order to work on code, create a fork from GitHub page. 
Use Git for cloning the code to your local or below line for Ubuntu:

	git clone <your-fork-git-link>

A directory called WordToVec will be created. Or you can use below link for exploring the code:

	git clone https://github.com/olcaytaner/WordToVec.git

## Open project with IntelliJ IDEA

Steps for opening the cloned project:

* Start IDE
* Select **File | Open** from main menu
* Choose `WordToVec/pom.xml` file
* Select open as project option
* Couple of seconds, dependencies with Maven will be downloaded. 


## Compile

**From IDE**

After being done with the downloading and Maven indexing, select **Build Project** option from **Build** menu. After compilation process, user can run WordToVec.

**From Console**

Go to `WordToVec` directory and compile with 

     mvn compile 

## Generating jar files

**From IDE**

Use `package` of 'Lifecycle' from maven window on the right and from `WordToVec` root module.

**From Console**

Use below line to generate jar file:

     mvn install



------------------------------------------------

WordToVec
============
+ [Maven Usage](#maven-usage)


### Maven Usage

    <groupId>NlpToolkit</groupId>
    <artifactId>WordToVec</artifactId>
    <version>1.0.0</version>
    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
    <repositories>
        <repository>
            <id>NlpToolkit</id>
            <url>http://haydut.isikun.edu.tr:8081/artifactory/NlpToolkit</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>Corpus</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>Dictionary</artifactId>
            <version>1.0.1</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>Math</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
