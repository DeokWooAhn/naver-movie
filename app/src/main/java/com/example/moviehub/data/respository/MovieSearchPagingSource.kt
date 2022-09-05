package com.example.moviehub.data.respository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviehub.data.api.RetrofitInstance.api
import com.example.moviehub.data.model.Item
import com.example.moviehub.util.Constants.PAGING_SIZE
import retrofit2.HttpException
import java.io.IOException

class MovieSearchPagingSource(
    private val query: String,
    private val display: Int
) : PagingSource<Int, Item>() {
    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return try {
            val pageNumber = params.key ?: STARTING_PAGE_INDEX
            val response = api.searchMovies(query, params.loadSize)
            val endOfPaginationReached = true

            val data = response.body()?.items!!
            val prevKey = if (pageNumber == STARTING_PAGE_INDEX) null else pageNumber - 1
            val nextKey = if (endOfPaginationReached) {
                null
            } else {
                pageNumber + (params.loadSize / PAGING_SIZE)
            }
            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }

}