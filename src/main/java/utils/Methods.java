package utils;

public class Methods {

    private String name;
    private String filePath;
    private int startLine;
    private int endLine;

    // Nuove metriche AST-based
    private int LOC;
    private int statementsCount;
    private int cyclomaticComplexity;
    private int nestingDepth;
    private int numberOfBranches;
    private int parameterCount;
    private int returnStatements;
    private int methodInvocations;
    private int distinctMethodInvocations;
    private int localVariableDeclarations;
    private boolean hasJavadoc;

    // Costruttore base
    public Methods(String name, int LOC) {
        this.name = name;
        this.LOC = LOC;
    }

    // Getter e Setter per tutti i campi:

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public int getLOC() {
        return LOC;
    }

    public void setLOC(int LOC) {
        this.LOC = LOC;
    }

    public int getStatementsCount() {
        return statementsCount;
    }

    public void setStatementsCount(int statementsCount) {
        this.statementsCount = statementsCount;
    }

    public int getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }

    public void setCyclomaticComplexity(int cyclomaticComplexity) {
        this.cyclomaticComplexity = cyclomaticComplexity;
    }

    public int getNestingDepth() {
        return nestingDepth;
    }

    public void setNestingDepth(int nestingDepth) {
        this.nestingDepth = nestingDepth;
    }

    public int getNumberOfBranches() {
        return numberOfBranches;
    }

    public void setNumberOfBranches(int numberOfBranches) {
        this.numberOfBranches = numberOfBranches;
    }

    public int getParameterCount() {
        return parameterCount;
    }

    public void setParameterCount(int parameterCount) {
        this.parameterCount = parameterCount;
    }

    public int getReturnStatements() {
        return returnStatements;
    }

    public void setReturnStatements(int returnStatements) {
        this.returnStatements = returnStatements;
    }

    public int getMethodInvocations() {
        return methodInvocations;
    }

    public void setMethodInvocations(int methodInvocations) {
        this.methodInvocations = methodInvocations;
    }

    public int getDistinctMethodInvocations() {
        return distinctMethodInvocations;
    }

    public void setDistinctMethodInvocations(int distinctMethodInvocations) {
        this.distinctMethodInvocations = distinctMethodInvocations;
    }

    public int getLocalVariableDeclarations() {
        return localVariableDeclarations;
    }

    public void setLocalVariableDeclarations(int localVariableDeclarations) {
        this.localVariableDeclarations = localVariableDeclarations;
    }

    public boolean getHasJavadoc() {
        return hasJavadoc;
    }

    public void setHasJavadoc(boolean hasJavadoc) {
        this.hasJavadoc = hasJavadoc;
    }
}
