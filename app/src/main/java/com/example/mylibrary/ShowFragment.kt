import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylibrary.Book

class ShowFragment : Fragment() {

    private val books: List<Book> = /* Obtén tus datos de la base de datos o donde los tengas */
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.see_layout, container, false)

        // Obtén la referencia al RecyclerView desde el diseño
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        // Configura el RecyclerView y asigna el adaptador
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = BookAdapter(books)

        return view
    }
}
