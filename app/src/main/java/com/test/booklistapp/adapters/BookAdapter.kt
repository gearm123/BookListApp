package com.test.booklistapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.booklistapp.R
import com.test.booklistapp.model.Book


open class BookAdapter :
    RecyclerView.Adapter<BookAdapter.DataViewHolder>(), Filterable {

    var bookList: ArrayList<Book> = ArrayList()
    var bookListFiltered: ArrayList<Book> = ArrayList()


    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView
        private val body: TextView
        private val icon: ImageView
        private val rating: RatingBar

        init {
            title = itemView.findViewById(R.id.title)
            body = itemView.findViewById(R.id.body)
            icon = itemView.findViewById(R.id.book_icon)
            rating = itemView.findViewById(R.id.book_rating)


            //TODO: add toast with position
            itemView.setOnClickListener {
            }
        }

        fun bind(result: Book) {
            title.text = result.title
            body.text = result.body
            Picasso.with(icon.context).load(result.downloadUrl).into(icon);
            rating.rating = result.rating.toFloat()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.book_item, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(bookListFiltered[position])
    }

    override fun getItemCount(): Int = bookListFiltered.size

    fun addData(list: MutableList<Book>) {
        bookList = list as ArrayList<Book>
        bookListFiltered = bookList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) bookListFiltered = bookList else {
                    val filteredList = ArrayList<Book>()
                    bookList
                        .filter {
                            (it.title.contains(constraint!!))

                        }
                        .forEach { filteredList.add(it) }
                    bookListFiltered = filteredList

                }
                return FilterResults().apply { values = bookListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                bookListFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Book>
                notifyDataSetChanged()
            }
        }
    }
}