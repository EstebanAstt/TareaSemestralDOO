package UI.Resources;

public interface SearchableItem {
    String getSearchableText();   // el texto contra el que se compara la búsqueda
    String getDisplayLabel();     // qué texto se muestra en la lista (ej. "Liga ABC — En curso")
}