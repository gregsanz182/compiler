function LDA(address){
    stack.push(new StackLine(address));
}
function LOD(address){
    stack.push(new StackLine(data[address].value));
}
function LDC(constant){
    stack.push(new StackLine(constant));
}
function STO(){
    let value = stack.pop().value;
    let address = stack.pop().value;
    data[address] = new DataLine(address,value);
    SP-=2;
}
function STN(){
    let value = stack.pop().value;
    let address = stack.pop().value;
    console.log(address);
    data[address] = new DataLine(address,value);
    SP-=2;
    stack.push(new StackLine(value));
}
function IXA(factor){
    // TODO: Change the compiler so the factor would be a number and not elem_size
    factor = 1;
    let address = stack.pop().value;
    let delta = stack.pop().value;
    stack.push(new StackLine(address+delta*factor));
}

function IND(delta){
    let address = stack.pop().value;
    SP--;
    let data_value = data[address + delta].value;
    stack.push(new StackLine(data_value));
}

function UJP(address){
    console.log(address-2);
    PC=address-2;
}

function FJP(address){
    let value = parseInt(stack.pop().value);   
    if (value==0) {
        console.log(address-2);
        PC=address-2;
    }   
}

function EQU(){
    let value1 = parseInt(stack.pop().value);
    let value2 = parseInt(stack.pop().value);
    SP-=2;
    if (value2==value1) {
        stack.push(new StackLine(1));
    }else{
        stack.push(new StackLine(0));
    }   
}

function GRT(){
    let value1 = parseInt(stack.pop().value);
    let value2 = parseInt(stack.pop().value);
    SP-=2;
    if (value2>value1) {
        stack.push(new StackLine(1));
    }else{
        stack.push(new StackLine(0));
    } 
}

function STP(){
    haltProgram();
}
function ADI(){
    let value1 = parseInt(stack.pop().value);
    let value2 = parseInt(stack.pop().value);
    SP-=2;
    stack.push(new StackLine(value2+value1));
}
function SBI(){
    let value1 = parseInt(stack.pop().value);
    let value2 = parseInt(stack.pop().value);
    console.log(value2-value1);
    SP-=2;
    stack.push(new StackLine(value2-value1));
}
function MPI(){
    let value1 = parseInt(stack.pop().value);
    let value2 = parseInt(stack.pop().value);
    SP-=2;
    stack.push(new StackLine(value2*value1));
}
function DVI(){
    let value1 = parseInt(stack.pop().value);
    let value2 = parseInt(stack.pop().value);
    SP-=2;
    if(value1 != 0)
        stack.push(new StackLine(value2/value1));
    else{
        haltProgram();
        $("#alert-container").append("<div class='alert alert-danger alert-dismissible' role='alert'>"+ 
                                    "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>"+
                                    "<span aria-hidden='true'>&times;</span></button>"+
                                    "<strong>Error de ejecución</strong>"+
                                    "</div>");
        
    }
}
function LAB(address){
    console.log("----Etiqueta-- "+address);
}
function WRI(){
    let value = parseInt(stack.pop().value);
    SP--;
    $('#console-body').append('Valor en tope de la pila: '+value+'<br>>&nbsp;');
}
function RDI(){
    let value = prompt('Indica el valor a leer: ');
    let address = parseInt(stack.pop().value);
    data[address] = new DataLine(address,value);
}