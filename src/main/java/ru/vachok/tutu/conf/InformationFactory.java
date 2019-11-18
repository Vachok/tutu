package ru.vachok.tutu.conf;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.vachok.tutu.parser.TrainInformer;


/**
 @since 16.11.2019 (10:52) */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass") public interface InformationFactory {
    
    
    @Contract(" -> new") static @NotNull InformationFactory getInstance() {
        return new TrainInformer();
    }
    
    String getInfo();
    
    void setClassOption(Object classOption);
    
    String getInfoAbout(Object aboutWhat);
    
}