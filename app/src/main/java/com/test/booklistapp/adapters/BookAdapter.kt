package com.test.booklistapp.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.booklistapp.R
import com.test.booklistapp.model.Book
import java.util.*


open class BookAdapter :
    RecyclerView.Adapter<BookAdapter.DataViewHolder>(), Filterable {

    var bookList: ArrayList<Book> = ArrayList()
    var bookListFiltered: ArrayList<Book> = ArrayList()
    var filterPattern: CharSequence = ""


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
        }

        //set list item properties
        fun bind(result: Book) {
            setTitleText(result, title)
            setBodyText(result, body)
            setBookIcon(result, icon)
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
        //bind items to filtered list
        holder.bind(bookListFiltered[position])
        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Book ${bookListFiltered[position].title} is chosen at index $position ",
                Toast.LENGTH_SHORT
            ).show()

        }
    }


    override fun getItemCount(): Int = bookListFiltered.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addData(list: MutableList<Book>) {
        bookList = list as ArrayList<Book>
        bookListFiltered = bookList
        notifyDataSetChanged()
    }

    //Filtering using constraint input by title and body
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                bookListFiltered = if (charString.isEmpty()) {
                    filterPattern = ""
                    bookList
                } else {
                    filterPattern = constraint.toString().lowercase(Locale.ROOT).trim();
                    val filteredList = ArrayList<Book>()
                    bookList
                        .filter {
                            (it.title.lowercase(Locale.ROOT).contains(
                                constraint.toString()
                                    .lowercase(Locale.ROOT)
                            )) or it.body.lowercase(Locale.ROOT).contains(
                                constraint.toString()
                                    .lowercase(Locale.ROOT)
                            )

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = bookListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                bookListFiltered = if (results?.values == null) {
                    ArrayList()
                } else {
                    results.values as ArrayList<Book>
                }
                notifyDataSetChanged()
            }
        }
    }

    //setting title text. highlighting part of text if needed
    private fun setTitleText(itemView: Book, titleTv: TextView) {
        if (filterPattern != "") {
            val startPos: Int =
                itemView.title.lowercase(Locale.ROOT)
                    .indexOf(filterPattern.toString().lowercase(Locale.ROOT))
            if (startPos != -1) {
                titleTv.text = createSpannable(startPos, itemView.title)
            } else {
                titleTv.text = itemView.title
            }
        } else {
            titleTv.text = itemView.title
        }
    }

    //setting body text. highlighting part of the text if needed
    private fun setBodyText(itemView: Book, bodyTv: TextView) {
        if (filterPattern != "") {
            val startPos: Int =
                itemView.body.lowercase(Locale.ROOT)
                    .indexOf(filterPattern.toString().lowercase(Locale.ROOT))
            if (startPos != -1) {
                bodyTv.text = createSpannable(startPos, itemView.body)
            } else {
                bodyTv.text = itemView.body
            }
        } else {
            bodyTv.text = itemView.body
        }
    }

    //Creating spannable to highlight filtered text
    private fun createSpannable(startPos: Int, baseText: String): Spannable {
        val endPos = startPos + filterPattern.length
        val spannable: Spannable = SpannableString(baseText)
        val blueColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(Color.BLUE))
        val highlightSpan = TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null)
        spannable.setSpan(
            highlightSpan,
            startPos,
            endPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    //Setting book icon using Picasso and placeholder using RGB values
    private fun setBookIcon(book: Book, icon: ImageView) {
        val placeHolderColor: Int = book.placeHolder
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(placeHolderColor)
        Picasso.with(icon.context).load(book.downloadUrl).placeholder(gradientDrawable).into(icon);
    }
}