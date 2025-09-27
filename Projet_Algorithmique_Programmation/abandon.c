

int abandon(int li, int col){ //0 si abandon, 1 sinon
    if(li==0 && col==0){
        return 0;
    }
    else{
        return 1;
    }
}


int main(){
    int ab = abandon(1,0);
    printf("%d\n", ab);
    return 0;
}
